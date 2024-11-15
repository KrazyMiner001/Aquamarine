package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.aquamarine.multiblocks.world.ChunkEventListener;
import tech.krazyminer001.aquamarine.multiblocks.world.ChunkEventListeners;

import java.util.*;

public class ShapeMatcher implements ChunkEventListener {
    private final BlockPos controllerPos;
    private final Direction controllerDirection;
    private final Map<BlockPos, SimpleMember> simpleMembers;
    private final Map<BlockPos, HatchFlags> hatchFlags;

    public ShapeMatcher(World world, BlockPos controllerPos, Direction controllerDirection, ShapeTemplate template) {
        this.controllerPos = controllerPos;
        this.controllerDirection = controllerDirection;
        this.simpleMembers = toWorldPos(controllerPos, controllerDirection, template.simpleMembers);
        this.hatchFlags = toWorldPos(controllerPos, controllerDirection, template.hatchFlags);
    }

    private boolean needsRematch = true;
    private boolean matchSuccessful = false;
    private List<HatchBlockEntity> matchedHatches = new ArrayList<>();

    public static BlockPos toWorldPos(BlockPos controllerPos, Direction controllerDirection, BlockPos templatePos) {
        BlockPos rotatedPos = switch (controllerDirection) {
            case NORTH -> new BlockPos(-templatePos.getX(), templatePos.getY(), templatePos.getZ());
            case SOUTH -> new BlockPos(templatePos.getX(), templatePos.getY(), -templatePos.getZ());
            case EAST -> new BlockPos(-templatePos.getZ(), templatePos.getY(), -templatePos.getX());
            default -> new BlockPos(templatePos.getZ(), templatePos.getY(), templatePos.getX());
        };
        return rotatedPos.add(controllerPos);
    }

    private static <V> Map<BlockPos, V> toWorldPos(BlockPos controllerPos, Direction controllerDirection, Map<BlockPos, V> templateMap) {
        Map<BlockPos, V> posMap = new HashMap<>();
        for (Map.Entry<BlockPos, V> entry : templateMap.entrySet()) {
            posMap.put(toWorldPos(controllerPos, controllerDirection, entry.getKey()), entry.getValue());
        }
        return posMap;
    }

    public Set<BlockPos> getPositions() {
        return simpleMembers.keySet();
    }

    public SimpleMember getSimpleMember(BlockPos pos) {
        return simpleMembers.get(pos);
    }

    public HatchFlags getHatchFlags(BlockPos pos) {
        return hatchFlags.get(pos);
    }

    public boolean needsRematch() {
        return needsRematch;
    }

    public boolean isMatchSuccessful() {
        return matchSuccessful && !needsRematch;
    }

    public List<HatchBlockEntity> getMatchedHatches() {
        return Collections.unmodifiableList(matchedHatches);
    }

    public void unlinkHatches() {
        matchedHatches.clear();
        matchSuccessful = false;
        needsRematch = true;
    }

    public void rematch(World world) {
        unlinkHatches();
        matchSuccessful = true;

        for (BlockPos pos : simpleMembers.keySet()) {
            ChunkPos chunkPos = new ChunkPos(pos);

            if (world.isChunkLoaded(chunkPos.x, chunkPos.z)) {

            }
            if (!matches(pos,world, matchedHatches)) {
                matchSuccessful = false;
                break;
            }
        }

        if (!matchSuccessful) {
            matchedHatches.clear();
        }

        needsRematch = false;
    }

    public boolean matches(BlockPos pos , World world, @Nullable List<HatchBlockEntity> hatches) {
        SimpleMember simpleMember = simpleMembers.get(pos);
        if (simpleMember == null) return false;

        BlockState state = world.getBlockState(pos);
        if (simpleMember.matchesState(state)) return true;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof HatchBlockEntity hatch) {
            HatchFlags flags = hatchFlags.get(pos);
            if (flags != null && flags.allows(hatch.getHatchType())) {
                if (matchedHatches != null) {
                    matchedHatches.add(hatch);
                }
                return true;
            }
        }

        return false;
    }



    public Set<ChunkPos> getSpannedChunks() {
        Set<ChunkPos> chunks = new HashSet<>();
        for (BlockPos pos : simpleMembers.keySet()) {
            chunks.add(new ChunkPos(pos));
        }
        return chunks;
    }

    public void registerListeners(World world) {
        for (ChunkPos chunkPos : getSpannedChunks()) {
            ChunkEventListeners.listeners.add(world, chunkPos, this);
        }
    }

    public void unregisterListeners(World world) {
        for (ChunkPos chunkPos : getSpannedChunks()) {
            ChunkEventListeners.listeners.remove(world, chunkPos, this);
        }
    }



    @Override
    public void onBlockUpdate(BlockPos pos) {
        if (simpleMembers.containsKey(pos)) {
            needsRematch = true;
        }
    }

    @Override
    public void onUnload() {
        needsRematch = false;
    }

    @Override
    public void onLoad() {
        needsRematch = true;
    }
}
