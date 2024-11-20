package tech.krazyminer001.aquamarine;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.aquamarine.example.ExampleBlocks;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;
import tech.krazyminer001.aquamarine.multiblocks.SimpleMember;
import tech.krazyminer001.aquamarine.multiblocks.world.ChunkEventListeners;
import tech.krazyminer001.aquamarine.networking.AquamarineData;
import tech.krazyminer001.aquamarine.networking.AquamarineS2CPacketSender;

import java.util.ArrayList;
import java.util.Collections;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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