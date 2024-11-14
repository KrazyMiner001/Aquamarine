package tech.krazyminer001.aquamarine;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.example.ExampleBlocks;
import tech.krazyminer001.aquamarine.multiblocks.world.ChunkEventListeners;

import java.util.ArrayList;
import java.util.Collections;

public class Aquamarine implements ModInitializer {
	public static final String MOD_ID = "aquamarine";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ArrayList<String> messages;

	@Override
	public void onInitialize() {
		LOGGER.info(messages.getFirst());

		ChunkEventListeners.init();

		ExampleBlocks.init();
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