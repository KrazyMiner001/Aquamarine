package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.blocks.FastBlockEntity;

public abstract class HatchBlockEntity extends FastBlockEntity {
    public HatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract HatchType getHatchType();

    protected void tickTransfer() {
    }

    public void tick() {
        if (world.isClient) return;

        tickTransfer();
        markDirty();
    }
}
