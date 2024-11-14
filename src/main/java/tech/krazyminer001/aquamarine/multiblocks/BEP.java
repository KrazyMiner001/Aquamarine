package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public record BEP(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
}
