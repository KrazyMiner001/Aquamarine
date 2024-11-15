package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.fluid.Fluid;

import java.util.List;

public class AquaFluidStorage extends AquaStorage<Fluid, FluidVariant, ConfigurableFluidStack>{
    public final FluidHandler fluidHandler = new FluidHandler();

    public AquaFluidStorage(List<ConfigurableFluidStack> storage) {
        super(storage, true);
    }

    public class FluidHandler {
        public int getNumSlots() {
            return storage.size();
        }

        public FluidStack getStack(int slot) {
            return storage.get(slot).toStack();
        }

        public FluidStack insertStack(int slot, FluidStack stack, boolean simulate) {
            if (stack.amount == 0) return FluidStack.EMPTY;

            ConfigurableStack<Fluid, FluidVariant> stackFluid = storage.get(slot);

            FluidVariant resource = stack.getFluidVariant();

            boolean canInsert;

            if (stackFluid.getAmount() == 0) {
                canInsert = true;
            } else {
                canInsert = stackFluid.getResource().equals(resource);
            }

            if (canInsert) {
                long inserted = Math.min(stack.getAmount(), stackFluid.getRemainingCapacityFor(resource));

                if (inserted > 0 && !simulate) {
                    stackFluid.add(inserted);
                    stackFluid.setResource(resource);
                }

                return inserted == stack.getAmount() ? FluidStack.EMPTY : new FluidStack(resource, stack.getAmount() - inserted);
            }

            return stack;
        }

        public void setStack(int slot, FluidStack stack) {
            ConfigurableStack<Fluid, FluidVariant> stackFluid = storage.get(slot);
            FluidVariant resource = stack.getFluidVariant();

            stackFluid.setResource(resource);
            stackFluid.setAmount(stack.getAmount());
        }

        public FluidStack extractStack(int slot, long amount, boolean simulate) {
            if (amount <= 0) return FluidStack.EMPTY;

            try (Transaction tx = Transaction.openOuter()) {
                FluidVariant resource = storage.get(slot).getResource();
                if (resource.isBlank()) return FluidStack.EMPTY;

                long result = storage.get(slot).extractDirect(resource, amount, tx);
                if (result > 0 && !simulate) {
                    tx.commit();
                }
                return result == 0 ? FluidStack.EMPTY : new FluidStack(resource, result);
            }
        }

        public FluidStack removeStack(int slot) {
            try (Transaction tx = Transaction.openOuter()) {
                ConfigurableStack<Fluid, FluidVariant> stackItem = storage.get(slot);
                FluidStack removedStack = new FluidStack(stackItem.getResource(), stackItem.getAmount());

                stackItem.setResource(new ConfigurableFluidStack(0).getBlank());
                stackItem.setAmount(0);
                tx.commit();
                return removedStack;
            }
        }
    }
}
