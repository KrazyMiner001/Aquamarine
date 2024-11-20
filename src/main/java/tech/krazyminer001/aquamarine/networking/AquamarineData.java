package tech.krazyminer001.aquamarine.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
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
}
