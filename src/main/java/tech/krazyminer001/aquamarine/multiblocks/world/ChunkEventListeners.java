package tech.krazyminer001.aquamarine.multiblocks.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import tech.krazyminer001.aquamarine.events.BlockStateReplacedCallback;

import java.util.Set;

/**
 * Manages all chunk listeners provided by Aquamarine.
 */
public class ChunkEventListeners {
    public static ChunkPosMultiMap<ChunkEventListener> listeners = new ChunkPosMultiMap<>();

    /**
     * Code to initialise the listeners.
     * Creates events for server stopping, chunks being loaded, chunks being unloaded, and blocks being updated.
     */
    public static void init() {
        ServerLifecycleEvents.SERVER_STOPPED.register(minecraftServer -> clearListeners());

        ServerChunkEvents.CHUNK_LOAD.register((serverWorld, worldChunk) -> {
            if (serverWorld.isClient()) return;
            ensureServerOnThread(serverWorld.getServer());

            Set<ChunkEventListener> cels = listeners.get(serverWorld, worldChunk.getPos());
            if (cels != null) {
                for (ChunkEventListener listener : cels) {
                    listener.onLoad();
                }
            }
        });

        ServerChunkEvents.CHUNK_UNLOAD.register((serverWorld, worldChunk) -> {
            if (serverWorld.isClient()) return;
            ensureServerOnThread(serverWorld.getServer());

            Set<ChunkEventListener> cels = listeners.get(serverWorld, worldChunk.getPos());
            if (cels != null) {
                for (ChunkEventListener listener : cels) {
                    listener.onUnload();
                }
            }
        });

        BlockStateReplacedCallback.EVENT.register((world, pos, state, moved) -> {
            if (world instanceof ServerWorld serverWorld && serverWorld.getServer().isOnThread()) {
                Set<ChunkEventListener> cels = listeners.get(serverWorld, new ChunkPos(pos));
                if (cels != null) {
                    for (ChunkEventListener listener : cels) {
                        listener.onBlockUpdate(pos);
                    }
                }
            }
        });
    }

    private static void clearListeners() {
        if (listeners.size() != 0) listeners = new ChunkPosMultiMap<>();
    }

    private static void ensureServerOnThread(MinecraftServer server) {
        if (server == null) throw new RuntimeException("Server is null");
        if (!server.isOnThread()) throw new RuntimeException("Server is not on-thread");
    }
}
