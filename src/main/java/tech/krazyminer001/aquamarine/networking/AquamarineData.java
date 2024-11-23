package tech.krazyminer001.aquamarine.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;

import static tech.krazyminer001.aquamarine.util.Utilities.of;

public class AquamarineData {
    // S2C Packets
    public record SetMultiblockRendererMultiblock(ShapeTemplate multiblock, BlockPos controllerPos, Direction controllerDirection) implements CustomPayload {
        public static final CustomPayload.Id<SetMultiblockRendererMultiblock> PACKET_ID = new CustomPayload.Id<>(of("set_multiblock_renderer_multiblock"));
        public static final PacketCodec<RegistryByteBuf, SetMultiblockRendererMultiblock> PACKET_CODEC = PacketCodec.tuple(
                ShapeTemplate.PACKET_CODEC, SetMultiblockRendererMultiblock::multiblock,
                BlockPos.PACKET_CODEC, SetMultiblockRendererMultiblock::controllerPos,
                Direction.PACKET_CODEC, SetMultiblockRendererMultiblock::controllerDirection,
                SetMultiblockRendererMultiblock::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    public record SetMultiblockRendererMultiblockLayered(ShapeTemplate multiblock, BlockPos controllerPos, Direction controllerDirection, boolean layered) implements CustomPayload {
        public static final CustomPayload.Id<SetMultiblockRendererMultiblockLayered> PACKET_ID = new CustomPayload.Id<>(of("set_multiblock_renderer_multiblock_layered"));
        public static final PacketCodec<RegistryByteBuf, SetMultiblockRendererMultiblockLayered> PACKET_CODEC = PacketCodec.tuple(
                ShapeTemplate.PACKET_CODEC, SetMultiblockRendererMultiblockLayered::multiblock,
                BlockPos.PACKET_CODEC, SetMultiblockRendererMultiblockLayered::controllerPos,
                Direction.PACKET_CODEC, SetMultiblockRendererMultiblockLayered::controllerDirection,
                PacketCodecs.BOOL, SetMultiblockRendererMultiblockLayered::layered,
                SetMultiblockRendererMultiblockLayered::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    public record SetMultiblockRendererMultiblockLayeredOffset(ShapeTemplate multiblock, BlockPos controllerPos, Direction controllerDirection, boolean layered, int offset) implements CustomPayload {
        public static final CustomPayload.Id<SetMultiblockRendererMultiblockLayeredOffset> PACKET_ID = new CustomPayload.Id<>(of("set_multiblock_renderer_multiblock_layered_offset"));
        public static final PacketCodec<RegistryByteBuf, SetMultiblockRendererMultiblockLayeredOffset> PACKET_CODEC = PacketCodec.tuple(
                ShapeTemplate.PACKET_CODEC, SetMultiblockRendererMultiblockLayeredOffset::multiblock,
                BlockPos.PACKET_CODEC, SetMultiblockRendererMultiblockLayeredOffset::controllerPos,
                Direction.PACKET_CODEC, SetMultiblockRendererMultiblockLayeredOffset::controllerDirection,
                PacketCodecs.BOOL, SetMultiblockRendererMultiblockLayeredOffset::layered,
                PacketCodecs.INTEGER, SetMultiblockRendererMultiblockLayeredOffset::offset,
                SetMultiblockRendererMultiblockLayeredOffset::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
