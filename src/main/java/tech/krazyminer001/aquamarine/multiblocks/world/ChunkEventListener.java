package tech.krazyminer001.aquamarine.multiblocks.world;

import net.minecraft.util.math.BlockPos;

public interface ChunkEventListener {
    void onBlockUpdate(BlockPos pos);

    void onUnload();

    void onLoad();
}
