package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurableStack<T, K extends TransferVariant<T>> extends SnapshotParticipant<ResourceAmount<K>> {
    protected K resource = getBlank();
    protected long amount = 0;
    private final Map<ChangeListener, Object> listeners = new HashMap<>();

    public ConfigurableStack() {

    }

    public ConfigurableStack(K resource, long amount) {
        this.resource = resource;
        this.amount = amount;
    }

    protected void notifyListeners() {
        ChangeListener.notify(listeners);
    }

    public void addListener(ChangeListener listener, Object token) {
        listeners.put(listener, token);
    }

    public void removeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    protected abstract K getBlank();

    public K getResource() {
        return resource;
    }

    public void setResource(K resource) {
        this.resource = resource;
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
