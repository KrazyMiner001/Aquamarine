package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;

public class ConfigurableItemStack extends ConfigurableStack<Item, ItemVariant> {
    private long capacity;

    public ConfigurableItemStack(ItemVariant resource, long amount) {
        super(resource, amount);
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    @Override
    protected long getRemainingCapacityFor(ItemVariant resource) {
        return capacity - amount;
    }
}
