package tech.krazyminer001.aquamarine.multiblocks;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ServiceLoader;

/**
 * Interface that deals with hatch types. To add extra hatch types create an enum that extends this class.
 */
public interface HatchType {
    PacketCodec<ByteBuf, HatchType> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(ByteBuf buf, HatchType value) {
            PacketCodecs.STRING.encode(buf, value.toString());
        }

        @Override
        public HatchType decode(ByteBuf buf) {
            return HatchType.DefaultHatchTypes.valueOf(PacketCodecs.STRING.decode(buf)); //Todo: make extendable
        }
    };

    HatchType ITEM_INPUT = DefaultHatchTypes.ITEM_INPUT;
    HatchType ITEM_OUTPUT = DefaultHatchTypes.ITEM_OUTPUT;
    HatchType FLUID_INPUT = DefaultHatchTypes.FLUID_INPUT;
    HatchType FLUID_OUTPUT = DefaultHatchTypes.FLUID_OUTPUT;
    HatchType ENERGY_INPUT = DefaultHatchTypes.ENERGY_INPUT;
    HatchType ENERGY_OUTPUT = DefaultHatchTypes.ENERGY_OUTPUT;

    /**
     * The hatch types provided by aquamarine by default.
     */
    enum DefaultHatchTypes implements HatchType {
        ITEM_INPUT,
        ITEM_OUTPUT,
        FLUID_INPUT,
        FLUID_OUTPUT,
        ENERGY_INPUT,
        ENERGY_OUTPUT;
    }
}
