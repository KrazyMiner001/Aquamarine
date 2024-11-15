package tech.krazyminer001.aquamarine.multiblocks.inventory;

import com.google.common.primitives.Ints;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AquaItemStorage extends AquaStorage<Item, ItemVariant, ConfigurableItemStack> {
    public final ItemHandler itemHandler = new ItemHandler();

    public AquaItemStorage(List<ConfigurableItemStack> stacks) {
        super(stacks, false);
    }

    public class ItemHandler {
        public int getNumSlots() {
            return storage.size();
        }

        public ItemStack getStack(int slot) {
            return storage.get(slot).getResource().toStack(Ints.saturatedCast(storage.get(slot).getAmount()));
        }

        public ItemStack setStack(int slot, ItemStack stack, boolean simulate) {
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

        public ItemStack removeStack(int slot, int amount, boolean simulate) {
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
    }
}
