package tech.krazyminer001.aquamarine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockStateReplacedCallback {
    Event<BlockStateReplacedCallback> EVENT = EventFactory.createArrayBacked(BlockStateReplacedCallback.class,
            (listeners) -> (world, pos, state, oldState) -> {
                for (BlockStateReplacedCallback listener : listeners) {
                    listener.onBlockStateReplaced(world, pos, state, oldState);
                }
            }
    );

    void onBlockStateReplaced(World world, BlockPos pos, BlockState state, boolean moved);
}
