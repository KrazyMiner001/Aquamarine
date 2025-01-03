package tech.krazyminer001.aquamarine.multiblocks;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains the types of hatches that may be accepted at a position.
 */
public class HatchFlags extends HashSet<HatchType> {
    public static final PacketCodec<ByteBuf, HatchFlags> PACKET_CODEC = new PacketCodec<>() {

        @Override
        public void encode(ByteBuf buf, HatchFlags value) {
            PacketCodecs.collection(
                    HashSet::newHashSet,
                    HatchType.PACKET_CODEC
            ).encode(buf, value);
        }

        @Override
        public HatchFlags decode(ByteBuf buf) {
            return null;
        }
    };

    private final Set<HatchType> flags;
    public static final HatchFlags EMPTY = new HatchFlags(Set.of());

    /**
     * Creates a new {@link HatchFlags} using a set of {@link HatchType}s
     * @param flags Set of hatch types that this flag allows.
     */
    public HatchFlags(Set<HatchType> flags) {
        this.flags = flags;
    }

    /**
     * @param type {@link HatchType} to test
     * @return Whether the {@link HatchType} is allowed by this flag.
     */
    public boolean allows(HatchType type) {
        return flags.contains(type);
    }

    /**
     * Builder to create a {@link HatchFlags} instance.
     */
    public static class Builder {
        private final Set<HatchType> flags = new HashSet<>();

        /**
         * Adds extra hatch type to the flags.
         * @param type the type to be added.
         */
        public Builder with(HatchType type) {
            flags.add(type);
            return this;
        }

        /**
         * Adds multiple hatch types to the flags.
         * @param types the types to add.
         */
        public Builder with(HatchType... types) {
            for (HatchType type : types) {
                with(type);
            }
            return this;
        }

        /**
         * Turns the builder into a {@link HatchFlags} instance.
         * @return the {@link HatchFlags} instance;
         */
        public HatchFlags build() {
            return new HatchFlags(flags);
        }
    }
}
