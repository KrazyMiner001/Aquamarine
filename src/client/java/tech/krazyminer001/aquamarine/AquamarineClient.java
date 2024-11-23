package tech.krazyminer001.aquamarine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.networking.AquamarineS2CPacketReceiver;
import tech.krazyminer001.aquamarine.rendering.HatchBlockOutlineRenderer;
import tech.krazyminer001.aquamarine.rendering.MultiblockRenderer;

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