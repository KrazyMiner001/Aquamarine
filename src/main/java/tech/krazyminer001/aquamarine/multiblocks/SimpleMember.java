package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Objects;
import java.util.function.Supplier;

public interface SimpleMember {
    /**
     * Returns whether the simple member matches a given {@link BlockState}.
     * @param state The {@link BlockState} to be checked for a match.
     * @return Whether the {@link BlockState} matches or not.
     */
    boolean matchesState(BlockState state);

    /**
     * Gets the {@link BlockState} of the member for multiblock previewing purposes.
     * @return A blockstate that is accepted by the member.
     */
    BlockState getPreviewState();

    /**
     * Creates a {@link SimpleMember} from a given {@link Block}.
     * @param block The {@link Block} to make the member from.
     * @return A {@link SimpleMember} which, for {@link SimpleMember#matchesState(BlockState)} returns true if and only if the {@link BlockState} is of the {@link Block} and returns the default state for {@link SimpleMember#getPreviewState()}.
     */
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

    /**
     * Creates a {@link SimpleMember} from a given {@link BlockState}.
     * @param state The {@link BlockState} to make the member from.
     * @return A {@link SimpleMember} which, for {@link SimpleMember#matchesState(BlockState)} returns true if and only if the {@link BlockState}s are the same and returns the given {@link BlockState} for {@link SimpleMember#getPreviewState()}.
     */
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
