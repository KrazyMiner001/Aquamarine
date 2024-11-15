package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.minecraft.util.Clearable;

public interface BlockEntityFluidInventory extends Clearable {
    AquaInventory getAquaInventory();

    default int size() {
        return getAquaInventory().getItemStorage().storage.size();
    }

    default boolean isEmpty() {
        return getAquaInventory().getItemStorage().storage.isEmpty();
    }

    default FluidStack removeStack(int slot, long amount) {
        return getAquaInventory().getFluidStorage().fluidHandler.extractStack(slot, amount, false);
    }

    default FluidStack getStack(int slot) {
        return getAquaInventory().getFluidStorage().fluidHandler.getStack(slot);
    }

    default FluidStack removeStack(int slot) {
        return getAquaInventory().getFluidStorage().fluidHandler.removeStack(slot);
    }

    default void setStack(int slot, FluidStack stack) {
        getAquaInventory().getFluidStorage().fluidHandler.setStack(slot, stack);
    }

    @Override
    default void clear() {

    }
}
