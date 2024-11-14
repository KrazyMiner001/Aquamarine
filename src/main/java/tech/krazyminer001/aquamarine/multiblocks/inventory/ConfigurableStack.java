package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public abstract class ConfigurableStack<T, K extends TransferVariant<T>> extends SnapshotParticipant<ResourceAmount<K>> {
    protected K resource;
    protected long amount;

    public ConfigurableStack(K resource, long amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public K getResource() {
        return resource;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void add(long amount) {
        this.amount += amount;
    }

    public void subtract(long amount) {
        this.amount -= amount;
    }

    @Override
    protected ResourceAmount<K> createSnapshot() {
        return new ResourceAmount<>(resource, amount);
    }

    @Override
    protected void readSnapshot(ResourceAmount<K> snapshot) {
        this.amount = snapshot.amount();
        this.resource = snapshot.resource();
    }

    public long extractDirect(K resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);
        if (resource.equals(this.resource)) {
            long extracted = Math.min(amount, maxAmount);
            updateSnapshots(transaction);
            subtract(extracted);
            return extracted;
        }
        return 0;
    }

    protected abstract long getRemainingCapacityFor(K resource);
}
