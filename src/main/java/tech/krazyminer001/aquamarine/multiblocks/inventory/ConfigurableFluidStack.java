package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;

/**
 * Implementation of {@link ConfigurableStack} for fluids.
 */
public class ConfigurableFluidStack extends ConfigurableStack<Fluid, FluidVariant> {
    private long capacity;

    public ConfigurableFluidStack(FluidVariant resource, long amount, long capacity) {
        super(resource, amount);
        this.capacity = capacity;
    }

    public ConfigurableFluidStack(long capacity) {
        super();
        this.capacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public FluidStack toStack() {
        return new FluidStack(resource, amount);
    }

    @Override
    protected FluidVariant getBlank() {
        return FluidVariant.blank();
    }

    @Override
    protected long getRemainingCapacityFor(FluidVariant resource) {
        return capacity - amount;
    }
}
