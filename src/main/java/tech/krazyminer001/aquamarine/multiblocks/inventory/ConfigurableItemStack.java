package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;

/**
 * An implementation of  {@link ConfigurableStack} for items.
 */
public class ConfigurableItemStack extends ConfigurableStack<Item, ItemVariant> {
    private long capacity = 64;

    public ConfigurableItemStack(ItemVariant resource, long amount) {
        super(resource, amount);
    }

    @Override
    protected ItemVariant getBlank() {
        return ItemVariant.blank();
    }


    public ConfigurableItemStack() {
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
