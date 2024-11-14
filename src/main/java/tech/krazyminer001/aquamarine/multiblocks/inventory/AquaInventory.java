package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AquaInventory {
    private final AquaItemStorage itemStorage;
    private final AquaFluidStorage fluidStorage;

    public AquaInventory(List<ConfigurableItemStack> itemStacks, List<ConfigurableFluidStack> fluidStacks) {
        this.itemStorage = new AquaItemStorage(new ArrayList<>(itemStacks));
        this.fluidStorage = new AquaFluidStorage(new ArrayList<>(fluidStacks));
    }
}
