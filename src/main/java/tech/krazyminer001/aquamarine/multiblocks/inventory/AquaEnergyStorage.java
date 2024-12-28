package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import java.util.List;

public class AquaEnergyStorage extends AquaStorage<EnergyStack, EnergyVariant, ConfigurableEnergyStack> {
    public AquaEnergyStorage(List<ConfigurableEnergyStack> storage) {
        super(storage, true);
    }

    public class EnergyHandler {
        public int getNumSlots() {
            return storage.size();
        }

        public EnergyStack getStack(int slot) {
            return storage.get(slot).toStack();
        }

        public EnergyStack insertStack(int slot, EnergyStack stackToInsert, boolean simulate) {
            if (stackToInsert.getAmount() == 0) return EnergyStack.BLANK;

            ConfigurableStack<EnergyStack, EnergyVariant> currentStackInSlot = storage.get(slot);

            EnergyVariant resource = EnergyVariant.of(stackToInsert);

            long inserted = Math.min(stackToInsert.getAmount(), currentStackInSlot.getRemainingCapacityFor(resource));

            if (inserted > 0 && !simulate) {
                currentStackInSlot.add(inserted);
                currentStackInSlot.setResource(resource);
            }

            return inserted == stackToInsert.getAmount() ? EnergyStack.BLANK : new EnergyStack(stackToInsert.getCapacity(), stackToInsert.getAmount() - inserted);
        }

    }

    public void setStack(int slot, EnergyStack stackToInsert) {
        ConfigurableEnergyStack currentStack = storage.get(slot);
        EnergyVariant resource = EnergyVariant.of(stackToInsert);

        currentStack.setAmount(stackToInsert.getAmount());
        currentStack.setResource(resource);
    }

    public EnergyStack extractStack(int slot, long amount, boolean simulate) {
        if (amount <= 0) return EnergyStack.BLANK;

        try (Transaction tx = Transaction.openOuter()) {
            EnergyVariant resource = storage.get(slot).getResource();
            if (resource.isBlank()) return EnergyStack.BLANK;

            long result = storage.get(slot).extractDirect(resource, amount, tx);
            if (result > 0 && !simulate) {
                tx.commit();
            }
            return result == 0 ? EnergyStack.BLANK : new EnergyStack(storage.get(slot).getResource().getCapacity(), result);
        }
    }

    public EnergyStack removeStack(int slot) {
        try (Transaction tx = Transaction.openOuter()) {
            ConfigurableEnergyStack stack = storage.get(slot);
            EnergyStack removedStack = new EnergyStack(stack.getResource().getCapacity(), stack.getAmount());

            stack.setResource(EnergyVariant.blank());
            stack.setAmount(0);
            tx.commit();
            return removedStack;
        }
    }
}
