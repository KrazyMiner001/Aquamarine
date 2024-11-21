package tech.krazyminer001.aquamarine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.multiblocks.*;
import tech.krazyminer001.aquamarine.networking.AquamarineS2CPacketReceiver;
import tech.krazyminer001.aquamarine.rendering.HatchBlockOutlineRenderer;
import tech.krazyminer001.aquamarine.rendering.MultiblockRenderer;

import java.util.Objects;

import static tech.krazyminer001.aquamarine.Aquamarine.MOD_ID;

public class AquamarineClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		WorldRenderEvents.LAST.register((worldRenderContext) -> {
			MultiblockRenderer.onRenderEvent(worldRenderContext.matrixStack());
		});

		ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
			MultiblockRenderer.clearMultiblock();
		});

		WorldRenderEvents.BLOCK_OUTLINE.register((worldRenderContext, blockOutlineContext) -> {
			HatchBlockOutlineRenderer.renderHatchBlockOutline(worldRenderContext, blockOutlineContext);

			return true;
        });

		AquamarineS2CPacketReceiver.registerS2CPacketReceivers();
	}
}