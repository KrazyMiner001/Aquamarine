package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.blocks.FastBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableEnergyStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableFluidStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableItemStack;

import java.util.List;

/**
 * A block entity that is used for creating hatches for multiblocks. Hatches are how multiblocks do IO.
 */
public abstract class HatchBlockEntity extends FastBlockEntity {

    public HatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * Gets the type of the hatch.
     * Must be overridden by inheriting class to indicate what hatch type it is.
     * @return Hatch type of the hatch.
     */
    public abstract HatchType getHatchType();

    protected void tickTransfer() {
    }

    /**
     * Code to be run by the block every tick.
     */
    public void tick() {
        if (world.isClient) return;

        tickTransfer();
        markDirty();
    }

    /**
     * Used by {@link tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory} to collect all item inputs.
     * @param list List for the item inputs to be added to.
     */
    public void appendItemInputs(List<ConfigurableItemStack> list) {
    }

    /**
     * Used by {@link tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory} to collect all item outputs.
     * @param list List for the item outputs to be added to.
     */
    public void appendItemOutputs(List<ConfigurableItemStack> list) {
    }

    /**
     * Used by {@link tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory} to collect all fluid inputs.
     * @param list List for the fluid inputs to be added to.
     */
    public void appendFluidInputs(List<ConfigurableFluidStack> list) {
    }

    /**
     * Used by {@link tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory} to collect all fluid outputs.
     * @param list List for the fluid outputs to be added to.
     */
    public void appendFluidOutputs(List<ConfigurableFluidStack> list) {
    }

    public void appendEnergyInputs(List<ConfigurableEnergyStack> list) {}

    public void appendEnergyOutputs(List<ConfigurableEnergyStack> list) {}
}
