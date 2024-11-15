package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface BlockEntityItemInventory extends Inventory {
    AquaInventory getAquaInventory();

    @Override
    default int size() {
        return getAquaInventory().getItemStorage().storage.size();
    }

    @Override
    default boolean isEmpty() {
        return getAquaInventory().getItemStorage().storage.isEmpty();
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        return getAquaInventory().getItemStorage().itemHandler.extractStack(slot, amount, false);
    }

    @Override
    default ItemStack getStack(int slot) {
        return getAquaInventory().getItemStorage().itemHandler.getStack(slot);
    }

    @Override
    default ItemStack removeStack(int slot) {
        return getAquaInventory().getItemStorage().itemHandler.removeStack(slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getAquaInventory().getItemStorage().itemHandler.setStack(slot, stack);
    }

    @Override
    default void clear() {
        getAquaInventory().getItemStorage().storage.clear();
    }
}
