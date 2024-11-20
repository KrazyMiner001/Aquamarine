package tech.krazyminer001.aquamarine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;
import tech.krazyminer001.aquamarine.multiblocks.SimpleMember;
import tech.krazyminer001.aquamarine.networking.AquamarineS2CPacketReceiver;
import tech.krazyminer001.aquamarine.rendering.MultiblockRenderer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
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

		AquamarineS2CPacketReceiver.registerS2CPacketReceivers();
	}
}