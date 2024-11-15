package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;

import java.util.List;

public class AquaFluidStorage extends AquaStorage<Fluid, FluidVariant, ConfigurableFluidStack>{

    public AquaFluidStorage(List<ConfigurableFluidStack> storage) {
        super(storage, true);
    }
}
