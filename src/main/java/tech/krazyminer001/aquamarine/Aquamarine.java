package tech.krazyminer001.aquamarine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.example.ExampleBlocks;
import tech.krazyminer001.aquamarine.multiblocks.world.ChunkEventListeners;
import tech.krazyminer001.aquamarine.networking.AquamarineData;

import java.util.ArrayList;
import java.util.Collections;

public class Aquamarine implements ModInitializer {
	public static final String MOD_ID = "aquamarine";

	public static MinecraftServer SERVER;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ArrayList<String> messages;

	@Override
	public void onInitialize() {
		LOGGER.info(messages.getFirst());

		ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);

		PayloadTypeRegistry.playS2C().register(
				AquamarineData.SetMultiblockRendererMultiblock.PACKET_ID,
				AquamarineData.SetMultiblockRendererMultiblock.PACKET_CODEC
		);

		ChunkEventListeners.init();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ExampleBlocks.init();
        }
    }

	static {
		messages = new ArrayList<>();
		messages.add("Hello World!");
		messages.add("Bet no-one is reading this.");
		messages.add("");
		messages.add("Hi!!!");

		Collections.shuffle(messages);
	}
}