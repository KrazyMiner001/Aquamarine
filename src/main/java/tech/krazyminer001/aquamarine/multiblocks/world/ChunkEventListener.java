package tech.krazyminer001.aquamarine.multiblocks.world;

import net.minecraft.util.math.BlockPos;

/**
 * Interface to listen to chunk events.
 */
public interface ChunkEventListener {
    /**
     * Code to be run when the listener is told a block updated.
     * @param pos Position of the block update.
     */
    void onBlockUpdate(BlockPos pos);

    /**
     * Code to be run when the listener is told the chunk it is listening in unloaded.
     */
    void onUnload();

    /**
     * Code to be run when the listener is told the chunk it is listening in loaded.
     */
    void onLoad();
}
