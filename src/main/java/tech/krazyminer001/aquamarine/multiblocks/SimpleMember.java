package tech.krazyminer001.aquamarine.multiblocks;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.GameVersion;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.*;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import tech.krazyminer001.aquamarine.Aquamarine;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Class to describe components of a multiblock that are just blocks without hatch functionality.
 */
public interface SimpleMember {
    PacketCodec<ByteBuf, SimpleMember> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(ByteBuf buf, SimpleMember value) {
            PacketCodecs.STRING.encode(buf, value.getType());
            buf.writeInt(Block.getRawIdFromState(value.getPreviewState()));
        }

        @Override
        public SimpleMember decode(ByteBuf buf) {
            String type = PacketCodecs.STRING.decode(buf);
            if (Objects.equals(type, "ofBlock")) {
                Block block = Objects.requireNonNull(Block.STATE_IDS.get(buf.readInt())).getBlock();

                return SimpleMember.ofBlock(() -> block);
            } else if (Objects.equals(type, "ofBlockState")) {
                return SimpleMember.ofBlockState(Block.STATE_IDS.get(buf.readInt()));
            }
            return null;
        }
    };

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

    String getType();

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

            @Override
            public String getType() {
                return "ofBlock";
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

            @Override
            public String getType() {
                return "ofBlockState";
            }
        };
    }
}
