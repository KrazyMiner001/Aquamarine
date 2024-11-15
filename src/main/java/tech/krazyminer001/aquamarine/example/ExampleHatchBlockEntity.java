package tech.krazyminer001.aquamarine.example;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.hatches.FluidHatch;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.BlockEntityFluidInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableFluidStack;

import java.util.List;

public class ExampleHatchBlockEntity extends FluidHatch implements BlockEntityFluidInventory {
    public ExampleHatchBlockEntity(BlockPos pos, BlockState state) {
        super(new BEP(ExampleBlocks.HATCH_BLOCK_ENTITY, pos, state), true, List.of(new ConfigurableFluidStack(1000)));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AquaInventory getAquaInventory() {
        return getInventory();
    }
}
