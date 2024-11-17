package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.fluid.Fluid;

import java.util.List;

/**
 * An implementation of {@link AquaStorage} for storing fluids.
 */
public class AquaFluidStorage extends AquaStorage<Fluid, FluidVariant, ConfigurableFluidStack>{
    public final FluidHandler fluidHandler = new FluidHandler();

    /**
     * Creates an instance based on provided item stacks.
     * @param storage A list of {@link ConfigurableFluidStack} to create the storage from.
     */
    public AquaFluidStorage(List<ConfigurableFluidStack> storage) {
        super(storage, true);
    }

    /**
     * A class to manage the items within a  {@link AquaFluidStorage}.
     * Preferred to be used over the methods from {@link net.fabricmc.fabric.api.transfer.v1.storage.Storage}
     */
    public class FluidHandler {
        public int getNumSlots() {
            return storage.size();
        }

        /**
         * Gets the fluid stack in a given slot.
         * @param slot The slot index to get the fluid from.
         * @return The fluid stack found at the slot with the given index.
         */
        public FluidStack getStack(int slot) {
            return storage.get(slot).toStack();
        }

        /**
         * Tries to insert a fluid stack into the storage
         * @param slot The index of the slot to insert fluids into.
         * @param stack The fluid stack to insert fluids from.
         * @param simulate Whether to commit changes at the end or just test the result.
         * @return A fluid stack containing the number of items that were in the original stack but could not be inserted. Has same fluid type or is empty.
         */
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

        /**
         * Sets the tack in the given slot to the given stack.
         * @param slot Index of the slot to set.
         * @param stack Stack for slot to be set to.
         */
        public void setStack(int slot, FluidStack stack) {
            ConfigurableStack<Fluid, FluidVariant> stackFluid = storage.get(slot);
            FluidVariant resource = stack.getFluidVariant();

            stackFluid.setResource(resource);
            stackFluid.setAmount(stack.getAmount());
        }

        /**
         * Tries to extract a set amount of items from a given slot.
         * @param slot The index of the slot to extract from.
         * @param amount The amount of items to be extracted.
         * @param simulate Whether to commit the changes at the end or to just test.
         * @return An item stack with the number of fluids extracted, and the fluid type that was extracted.
         */
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

        /**
         * Sets the fluid stack in a slot to be empty.
         * @param slot Index of slot to be removed from.
         * @return The fluid stack that was in the slot before it was removed.
         */
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
