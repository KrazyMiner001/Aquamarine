package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AquaInventory {
    private final AquaItemStorage itemStorage;
    private final AquaFluidStorage fluidStorage;

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

    public List<ConfigurableItemStack> getItems() {
        return itemStorage.storage;
    }

    public List<ConfigurableFluidStack> getFluids() {
        return fluidStorage.storage;
    }
}
