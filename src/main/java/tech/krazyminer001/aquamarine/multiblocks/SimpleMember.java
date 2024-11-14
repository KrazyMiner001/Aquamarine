package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Objects;
import java.util.function.Supplier;

public interface SimpleMember {
    boolean matchesState(BlockState state);

    BlockState getPreviewState();

    static SimpleMember ofBlock(Supplier<? extends Block> block) {
        Objects.requireNonNull(block);

        return new SimpleMember() {
            @Override
            public boolean matchesState(BlockState state) {
                return state.isOf(block.get());
            }

            @Override
            public BlockState getPreviewState() {
                return block.get().getDefaultState();
            }
        };
    }

    static SimpleMember ofBlockState(BlockState state) {
        Objects.requireNonNull(state);

        return new SimpleMember() {

            @Override
            public boolean matchesState(BlockState state2) {
                return state.equals(state2);
            }

            @Override
            public BlockState getPreviewState() {
                return state;
            }
        };
    }
}
