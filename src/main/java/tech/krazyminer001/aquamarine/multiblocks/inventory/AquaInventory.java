package tech.krazyminer001.aquamarine.multiblocks.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing different types of storage with methods to access the storage.
 * Can be extended to add more storage types if more storage types are added.
 */
public class AquaInventory {
    private final AquaItemStorage itemStorage;
    private final AquaFluidStorage fluidStorage;

    /**
     * Creates a {@link AquaInventory} instance from a list of item stacks and fluid stacks.
     * @param itemStacks A list of item stacks in the form of {@link ConfigurableItemStack}s.
     * @param fluidStacks A list of fluid stacks in the form of {@link ConfigurableFluidStack}s.
     */
    public AquaInventory(List<ConfigurableItemStack> itemStacks, List<ConfigurableFluidStack> fluidStacks) {
        this.itemStorage = new AquaItemStorage(new ArrayList<>(itemStacks));
        this.fluidStorage = new AquaFluidStorage(new ArrayList<>(fluidStacks));
    }

    public AquaFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    public AquaItemStorage getItemStorage() {
        return itemStorage;
    }

    /**
     * Gets the list of stacks from the item storage.
     */
    public List<ConfigurableItemStack> getItems() {
        return itemStorage.storage;
    }

    /**
     * Gets the list of stacks from the fluid storage.
     */
    public List<ConfigurableFluidStack> getFluids() {
        return fluidStorage.storage;
    }
}
