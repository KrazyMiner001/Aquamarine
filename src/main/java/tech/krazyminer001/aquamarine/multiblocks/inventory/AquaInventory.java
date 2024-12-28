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
    private final AquaEnergyStorage energyStorage;

    /**
     * Creates a {@link AquaInventory} instance from a list of item stacks and fluid stacks.
     * @param itemStacks A list of item stacks in the form of {@link ConfigurableItemStack}s.
     * @param fluidStacks A list of fluid stacks in the form of {@link ConfigurableFluidStack}s.
     */
    private AquaInventory(List<ConfigurableItemStack> itemStacks, List<ConfigurableFluidStack> fluidStacks, List<ConfigurableEnergyStack> energyStacks) {
        this.itemStorage = new AquaItemStorage(new ArrayList<>(itemStacks));
        this.fluidStorage = new AquaFluidStorage(new ArrayList<>(fluidStacks));
        this.energyStorage = new AquaEnergyStorage(new ArrayList<>(energyStacks));
    }

    public AquaFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    public AquaItemStorage getItemStorage() {
        return itemStorage;
    }

    public AquaEnergyStorage getEnergyStorage() {
        return energyStorage;
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

    public List<ConfigurableEnergyStack> getEnergy() {
        return energyStorage.storage;
    }

    public static class Builder {
        private final List<ConfigurableItemStack> itemStacks = new ArrayList<>();
        private final List<ConfigurableFluidStack> fluidStacks = new ArrayList<>();
        private final List<ConfigurableEnergyStack> energyStacks = new ArrayList<>();

        public Builder() {}

        public Builder addItemInventory(AquaItemStorage storage) {
            itemStacks.addAll(storage.storage);
            return this;
        }

        public Builder addFluidInventory(AquaFluidStorage storage) {
            fluidStacks.addAll(storage.storage);
            return this;
        }

        public Builder addEnergyInventory(AquaEnergyStorage storage) {
            energyStacks.addAll(storage.storage);
            return this;
        }

        public Builder addItemStacks(List<ConfigurableItemStack> stacks) {
            itemStacks.addAll(stacks);
            return this;
        }

        public Builder addFluidStacks(List<ConfigurableFluidStack> stacks) {
            fluidStacks.addAll(stacks);
            return this;
        }

        public Builder addEnergyStacks(List<ConfigurableEnergyStack> stacks) {
            energyStacks.addAll(stacks);
            return this;
        }

        public Builder addItemStack(ConfigurableItemStack stack) {
            itemStacks.add(stack);
            return this;
        }

        public Builder addFluidStack(ConfigurableFluidStack stack) {
            fluidStacks.add(stack);
            return this;
        }

        public Builder addEnergyStack(ConfigurableEnergyStack stack) {
            energyStacks.add(stack);
            return this;
        }

        public AquaInventory build() {
            return new AquaInventory(itemStacks, fluidStacks, energyStacks);
        }
    }
}
