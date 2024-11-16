/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package tech.krazyminer001.aquamarine.multiblocks.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A map meant for {@link ChunkEventListeners} that maps a {@link WorldAccess} to a map that maps a {@link ChunkPos} to a set of {@link T}.
 * @param <T> An object that a {@link ChunkPos} can have multiple associated with it.
 */
public class ChunkPosMultiMap<T> {
    private final Map<WorldAccess, HashMap<ChunkPos, Set<T>>> storage = new HashMap<>();

    /**
     * Adds a {@link T} to the map with a corresponding world and chunk position.
     * Adds the world and chunk position to map if needed.
     * @param world The {@link WorldAccess} for the world.
     * @param chunkPos The {@link ChunkPos} the {@link T} is for.
     * @param t The {@link T} object to be added.
     */
    public final void add(WorldAccess world, ChunkPos chunkPos, T t) {
        storage.computeIfAbsent(world, w -> new HashMap<>()).computeIfAbsent(chunkPos, p -> new HashSet<>()).add(t);
    }

    /**
     * Removes a {@link T} to the map with a corresponding world and chunk position.
     * Removes the world and chunk position if made empty after removal of the {@link T}.
     * @param world The {@link WorldAccess} for the world.
     * @param chunkPos The {@link ChunkPos} the {@link T} is for.
     * @param t The {@link T} object to be added.
     */
    public final void remove(WorldAccess world, ChunkPos chunkPos, T t) {
        Map<ChunkPos, Set<T>> chunkPosMap = storage.get(world);
        Set<T> tSet = chunkPosMap.get(chunkPos);

        if (!tSet.remove(t)) {
            throw new RuntimeException("Could not remove element at position " + chunkPos + " as it does not exist.");
        }

        if (tSet.isEmpty()) {
            chunkPosMap.remove(chunkPos);

            if (chunkPosMap.isEmpty()) {
                storage.remove(world);
            }
        }
    }

    /**
     * Gets a collection of all the {@link T} for a given world and chunk position.
     * @param world The {@link WorldAccess} for the world.
     * @param chunkPos The position of the chunk to be checked.
     * @return A set of all {@link T} at the given world and chunk position.
     */
    @Nullable
    public final Set<T> get(WorldAccess world, ChunkPos chunkPos) {
        Map<ChunkPos, Set<T>> chunkPosSetMap = storage.get(world);
        if (chunkPosSetMap == null) {
            return null;
        }
        return chunkPosSetMap.get(chunkPos);
    }

    public final int size() {
        return storage.size();
    }
}