package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

/**
 * Block Entity Parameters. Simply makes block entities need less parameters.
 * @param blockEntityType Block Entity Type for block entity.
 * @param pos Position for block entity.
 * @param state BlockState for block entity.
 */
public record BEP(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
}
