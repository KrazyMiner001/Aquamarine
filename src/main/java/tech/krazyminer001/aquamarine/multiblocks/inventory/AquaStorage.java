package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;
import java.util.List;

/**
 *  * A class for storing Objects.
 *  Mostly meant to allow for easier transport between machines.
 *  * @param <T> The type of the storage.
 *  * @param <K> The {@link TransferVariant} for the storage type.
 *  * @param <O> The {@link ConfigurableStack} for the storage type and {@link TransferVariant}
 *  */
public class AquaStorage <T, K extends TransferVariant<T>, O extends ConfigurableStack<T, K>> implements Storage<K> {
    public final List<O> storage;
    private final boolean oneSlotPerResource;

    /**
     * Creates a storage instance.
     * @param storage A {@link ConfigurableStack}s to be stored.
     * @param oneSlotPerResource Whether a resource only occupies one slot or if it can use multiple.
     *                           By default, true for fluids, false for items.
     */
    public AquaStorage(List<O> storage, boolean oneSlotPerResource) {
        this.storage = storage;
        this.oneSlotPerResource = oneSlotPerResource;
    }

    @Override
    public long insert(K resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        boolean containsResourceAlready = false;
        long amountInserted = 0;

        for (O stack : storage) {
            containsResourceAlready |= stack.getResource().equals(resource);
        }

        for (O stack : storage) {
            boolean slotEmpty = stack.getAmount() == 0;
            boolean canInsert;
            if (slotEmpty) {
                if (oneSlotPerResource) {
                    canInsert = !containsResourceAlready;
                } else {
                    canInsert = true;
                }
            } else {
                canInsert = stack.getResource().equals(resource);
            }

            if (canInsert) {
                long inserted = Math.min(maxAmount - amountInserted, stack.getRemainingCapacityFor(resource));

                if (inserted > 0) {
                    stack.updateSnapshots(transaction);
                    stack.add(inserted);
                    amountInserted += inserted;
                }
            }

            if (maxAmount - amountInserted == 0) {break;}
        }

        return amountInserted;
    }

    @Override
    public long extract(K resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        boolean containsResource  = false;
        for (O stack : storage) {
            containsResource |= stack.getResource().equals(resource);
        }
        if (!containsResource) return 0;

        long amountExtracted = 0;

        for (O stack : storage) {
            if (stack.getResource().equals(resource)) {
                long extracted = Math.min(maxAmount - amountExtracted, stack.getRemainingCapacityFor(resource));

                if (extracted > 0) {
                    stack.updateSnapshots(transaction);
                    stack.subtract(extracted);
                    amountExtracted += extracted;
                }
            }

            if (maxAmount - amountExtracted == 0) break;
        }

        return amountExtracted;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) // Modern Industrialisation does this to it's probably fine
    @Override
    public Iterator<StorageView<K>> iterator() {
        return (Iterator) storage.iterator();
    }

    public boolean canInsert(K resource, long maxAmount) {
        return true;
    }
}
