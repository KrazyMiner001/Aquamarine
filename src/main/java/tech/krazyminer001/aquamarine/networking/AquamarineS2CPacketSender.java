package tech.krazyminer001.aquamarine.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;

public class AquamarineS2CPacketSender {
    public static void sendSetMultiblockRendererData(ShapeTemplate shapeTemplate, BlockPos controllerPos, Direction controllerDirection, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new AquamarineData.SetMultiblockRendererMultiblock(shapeTemplate, controllerPos, controllerDirection));
    }

    public static void sendSetMultiblockRendererData(ShapeTemplate shapeTemplate, BlockPos controllerPos, Direction controllerDirection, boolean layered, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new AquamarineData.SetMultiblockRendererMultiblockLayered(shapeTemplate, controllerPos, controllerDirection, layered));
    }

    public static void sendSetMultiblockRendererData(ShapeTemplate shapeTemplate, BlockPos controllerPos, Direction controllerDirection, boolean layered, int offset, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new AquamarineData.SetMultiblockRendererMultiblockLayeredOffset(shapeTemplate, controllerPos, controllerDirection, layered, offset));
    }
}
