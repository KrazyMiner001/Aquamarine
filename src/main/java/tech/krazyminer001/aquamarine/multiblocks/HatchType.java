package tech.krazyminer001.aquamarine.multiblocks;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * Interface that deals with hatch types. To add extra hatch types create an enum that extends this class.
 */
public class HatchType {
      public static final PacketCodec<ByteBuf, HatchType> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(ByteBuf buf, HatchType value) {
            PacketCodecs.STRING.encode(buf, value.toString());
        }

        @Override
        public HatchType decode(ByteBuf buf) {
            return new HatchType(PacketCodecs.STRING.decode(buf));
        }
    };

    private final String name;

    protected HatchType(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HatchType)) {return false;}
        return name.equals(((HatchType) obj).name);
    }

    public static final HatchType ITEM_INPUT = new HatchType("ITEM_INPUT");
    public static final HatchType ITEM_OUTPUT = new HatchType("ITEM_OUTPUT");
    public static final HatchType FLUID_INPUT = new HatchType("FLUID_INPUT");
    public static final HatchType FLUID_OUTPUT = new HatchType("FLUID_OUTPUT");
    public static final HatchType ENERGY_INPUT = new HatchType("ENERGY_INPUT");
    public static final HatchType ENERGY_OUTPUT = new HatchType("ENERGY_OUTPUT");
}
