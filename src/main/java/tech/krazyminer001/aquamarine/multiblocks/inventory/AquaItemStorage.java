package tech.krazyminer001.aquamarine.multiblocks.inventory;

import com.google.common.primitives.Ints;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * An implementation of {@link AquaStorage} for storing items.
 */
public class AquaItemStorage extends AquaStorage<Item, ItemVariant, ConfigurableItemStack> {
    public final ItemHandler itemHandler = new ItemHandler();

    /**
     * Creates an instance based on provided item stacks.
     * @param stacks A list of {@link ConfigurableItemStack} to create the storage from.
     */
    public AquaItemStorage(List<ConfigurableItemStack> stacks) {
        super(stacks, false);
    }

    /**
     * A class to manage the items within a {@link AquaItemStorage}.
     * Preferred to be used over the methods from {@link net.fabricmc.fabric.api.transfer.v1.storage.Storage}.
     */
    public class ItemHandler {
        public int getNumSlots() {
            return storage.size();
        }

        /**
         * Gets the item stack in a given slot.
         * @param slot The slot index to get the item from.
         * @return The item stack found at the slot with the given index.
         */
        public ItemStack getStack(int slot) {
            return storage.get(slot).getResource().toStack(Ints.saturatedCast(storage.get(slot).getAmount()));
        }

        /**
         * Tries to insert a stack of items into the storage.
         * @param slot The index of the slot to insert items into.
         * @param stack The item stack to insert items from.
         * @param simulate Whether to commit changes at the end or just test the result.
         * @return An item stack containing the number of items that were in the original stack but could not be inserted. Has the same item type or is empty.
         */
        public ItemStack insertStack(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            ConfigurableStack<Item, ItemVariant> stackItem = storage.get(slot);

            ItemVariant resource = ItemVariant.of(stack);

            boolean canInsert;

            if (stackItem.getAmount() == 0) {
                canInsert = true;
            } else {
                canInsert = stackItem.getResource().equals(resource);
            }

            if (canInsert) {
                long inserted = Math.min(stack.getCount(), stackItem.getRemainingCapacityFor(resource));

                if (inserted > 0 && !simulate) {
                    stackItem.add(inserted);
                    stackItem.setResource(resource);
                }

                return inserted == stack.getCount() ? ItemStack.EMPTY : resource.toStack((int) (stack.getCount() - inserted));
            }

            return stack;
        }

        /**
         * Sets the stack in the given slot to be the given stack.
         * @param slot Index of slot to set.
         * @param stack Stack for slot to be set to.
         */
        public void setStack(int slot, ItemStack stack) {
            ConfigurableStack<Item, ItemVariant> stackItem = storage.get(slot);
            ItemVariant resource = ItemVariant.of(stack);

            stackItem.setResource(resource);
            stackItem.setAmount(stack.getCount());
        }

        /**
         * Tries to extract a set amount of items from a given slot.
         * @param slot The index of the slot to extract from.
         * @param amount The amount of items to be extracted.
         * @param simulate Whether to commit changes at the end or to just test.
         * @return An item stack with the number of items extracted, and the item type that was extracted.
         */
        public ItemStack extractStack(int slot, int amount, boolean simulate) {
            if (amount <= 0) return ItemStack.EMPTY;

            try (Transaction tx = Transaction.openOuter()) {
                ItemVariant resource = storage.get(slot).getResource();
                if (resource.isBlank()) return ItemStack.EMPTY;

                long result = storage.get(slot).extractDirect(resource, amount, tx);
                if (result > 0 && !simulate) {
                    tx.commit();
                }
                return result == 0 ? ItemStack.EMPTY : resource.toStack((int) result);
            }
        }

        /**
         * Sets the item stack in a slot to be empty.
         * @param slot Index of slot to be removed from.
         * @return The item stack that was in the slot before it was removed.
         */
        public ItemStack removeStack(int slot) {
            try (Transaction tx = Transaction.openOuter()) {
                ConfigurableStack<Item, ItemVariant> stackItem = storage.get(slot);
                ItemStack removedStack = stackItem.getResource().toStack((int) stackItem.getAmount());

                stackItem.setResource(new ConfigurableItemStack().getBlank());
                stackItem.setAmount(0);
                tx.commit();
                return removedStack;
            }
        }
    }
}
