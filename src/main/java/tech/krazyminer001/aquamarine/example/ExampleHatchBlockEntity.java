package tech.krazyminer001.aquamarine.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.hatches.ItemHatch;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExampleHatchBlockEntity extends ItemHatch {
    public ExampleHatchBlockEntity(BlockPos pos, BlockState state) {
        super(new BEP(ExampleBlocks.HATCH_BLOCK_ENTITY, pos, state), true, new AquaInventory(
                List.of(new ConfigurableItemStack()),
                List.of()
        ));
    }
}
