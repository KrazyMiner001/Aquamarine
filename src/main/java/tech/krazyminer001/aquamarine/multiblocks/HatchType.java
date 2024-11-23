package tech.krazyminer001.aquamarine.multiblocks;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

import static tech.krazyminer001.aquamarine.util.Utilities.of;

/**
 * Interface that deals with hatch types. To add extra hatch types create an enum that extends this class.
 */
public class HatchType {
      public static final PacketCodec<ByteBuf, HatchType> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(ByteBuf buf, HatchType value) {
            Identifier.PACKET_CODEC.encode(buf, value.getId());
        }

        @Override
        public HatchType decode(ByteBuf buf) {
            return new HatchType(Identifier.PACKET_CODEC.decode(buf));
        }
    };

    private final Identifier id;

    protected HatchType(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HatchType)) {return false;}
        return id.equals(((HatchType) obj).id);
    }

    public static final HatchType ITEM_INPUT = new HatchType(of("ITEM_INPUT"));
    public static final HatchType ITEM_OUTPUT = new HatchType(of("ITEM_OUTPUT"));
    public static final HatchType FLUID_INPUT = new HatchType(of("FLUID_INPUT"));
    public static final HatchType FLUID_OUTPUT = new HatchType(of("FLUID_OUTPUT"));
    public static final HatchType TR_ENERGY_INPUT = new HatchType(of("TR_ENERGY_INPUT"));
    public static final HatchType TR_ENERGY_OUTPUT = new HatchType(of("TR_ENERGY_OUTPUT"));
}
