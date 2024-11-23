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

/**
 * Class to detect if a multiblock is valid, and to give access to hatches within the multiblock structure.
 */
public class ShapeMatcher implements ChunkEventListener {
    private final BlockPos controllerPos;
    private final Direction controllerDirection;
    private final Map<BlockPos, SimpleMember> simpleMembers;
    private final Map<BlockPos, HatchFlags> hatchFlags;

    /**
     * Creates a {@link ShapeMatcher} based on a world, the position of the controller, the direction of the controller, and a template.
     * @param world The world the multiblock controller is in.
     * @param controllerPos The position of the multiblock controller.
     * @param controllerDirection The direction the controller is facing.
     * @param template The {@link ShapeTemplate} describing the shape of the multiblock.
     */
    public ShapeMatcher(World world, BlockPos controllerPos, Direction controllerDirection, ShapeTemplate template) {
        this.controllerPos = controllerPos;
        this.controllerDirection = controllerDirection;
        this.simpleMembers = toWorldPos(controllerPos, controllerDirection, template.simpleMembers);
        this.hatchFlags = toWorldPos(controllerPos, controllerDirection, template.hatchFlags);
    }

    private boolean needsRematch = true;
    private boolean matchSuccessful = false;
    private List<HatchBlockEntity> matchedHatches = new ArrayList<>();

    /**
     * Gets the world position for a relative block position from the template.
     * @param controllerPos Position of the controller of the multiblock
     * @param controllerDirection Direction of the controller of the multiblock.
     * @param templatePos The relative position, from the template.
     * @return The position within the world of the provided relative position for a multiblock with a controller at the provided position with the provided direction.
     */
    public static BlockPos toWorldPos(BlockPos controllerPos, Direction controllerDirection, BlockPos templatePos) {
        BlockPos rotatedPos = switch (controllerDirection) {
            case NORTH -> new BlockPos(-templatePos.getX(), templatePos.getY(), templatePos.getZ());
            case SOUTH -> new BlockPos(templatePos.getX(), templatePos.getY(), -templatePos.getZ());
            case EAST -> new BlockPos(-templatePos.getZ(), templatePos.getY(), -templatePos.getX());
            default -> new BlockPos(templatePos.getZ(), templatePos.getY(), templatePos.getX());
        };
        return rotatedPos.add(controllerPos);
    }

    /**
     * Takes a map of relative block positions and an object to make a map of world block positions with the same object.
     * @param controllerPos The position of the multiblock controller.
     * @param controllerDirection The direction of the multiblock controller.
     * @param templateMap Map of template block positions and an object {@link V}.
     * @return Map of world positions from template block positions with the same {@link V}
     * @param <V> An object that is used in the block position maps.
     */
    public static <V> Map<BlockPos, V> toWorldPos(BlockPos controllerPos, Direction controllerDirection, Map<BlockPos, V> templateMap) {
        Map<BlockPos, V> posMap = new HashMap<>();
        for (Map.Entry<BlockPos, V> entry : templateMap.entrySet()) {
            posMap.put(toWorldPos(controllerPos, controllerDirection, entry.getKey()), entry.getValue());
        }
        return posMap;
    }

    public Set<BlockPos> getPositions() {
        return simpleMembers.keySet();
    }

    /**
     * Gets the {@link SimpleMember} at a position.
     * @param pos The position to check.
     * @return The simple member at that position.
     */
    public SimpleMember getSimpleMember(BlockPos pos) {
        return simpleMembers.get(pos);
    }

    /**
     * Gets the {@link HatchFlags} a block may have at a position.
     * @param pos The position to check.
     * @return The hatch flags at that position.
     */
    public HatchFlags getHatchFlags(BlockPos pos) {
        return hatchFlags.get(pos);
    }

    /**
     * Gets if the muliblock needs to be rematched.
     * @return Whether the multiblock needs to be rematched or not.
     */
    public boolean needsRematch() {
        return needsRematch;
    }

    /**
     * Checks whether the multiblock is matched or not.
     * @return If the multiblock is succesfully matched.
     */
    public boolean isMatchSuccessful() {
        return matchSuccessful && !needsRematch;
    }

    public List<HatchBlockEntity> getMatchedHatches() {
        return Collections.unmodifiableList(matchedHatches);
    }

    /**
     * Unlinks all matched {@link HatchBlockEntity}s. Sets that the multiblock needs to be rematched.
     */
    public void unlinkHatches() {
        matchedHatches.clear();
        matchSuccessful = false;
        needsRematch = true;
    }

    /**
     * Attempts to rematch the multiblock.
     * @param world The world to attempt the match in.
     */
    public void rematch(World world) {
        unlinkHatches();
        matchSuccessful = true;

        for (BlockPos pos : simpleMembers.keySet()) {
            ChunkPos chunkPos = new ChunkPos(pos);

            if (world.isChunkLoaded(chunkPos.x, chunkPos.z)) {

            }
            if (!matches(pos, world, matchedHatches)) {
                matchSuccessful = false;
                break;
            }
        }

        if (!matchSuccessful) {
            matchedHatches.clear();
        }

        needsRematch = false;
    }

    /**
     * Determines whether a block is a valid block within the multiblock at a position.
     * Used to test if the entire multiblock matches.
     * If there is a matched hatch, the hatch is added to the list of hatches.
     * @param pos Position to test.
     * @param world World to test in.
     * @param hatches List of hatches that hatches get added to.
     * @return True if matches.
     */
    public boolean matches(BlockPos pos , World world, @Nullable List<HatchBlockEntity> hatches) {
        SimpleMember simpleMember = simpleMembers.get(pos);
        if (simpleMember == null) return false;

        BlockState state = world.getBlockState(pos);
        if (simpleMember.matchesState(state)) return true;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof HatchBlockEntity hatch) {
            HatchFlags flags = hatchFlags.get(pos);
            if (flags != null && flags.allows(hatch.getHatchType())) {
                if (hatches != null) {
                    hatches.add(hatch);
                }
                return true;
            }
        }

        return false;
    }


    /**
     * Gets all chunks that the multiblock goes over. Used to register listeners for each chunk.
     * @return Set of {@link ChunkPos}s for each chunk that the multiblock spans.
     */
    public Set<ChunkPos> getSpannedChunks() {
        Set<ChunkPos> chunks = new HashSet<>();
        for (BlockPos pos : simpleMembers.keySet()) {
            chunks.add(new ChunkPos(pos));
        }
        return chunks;
    }

    /**
     * Adds chunk event listeners for each chunk that the multiblock spans.
     * @param world The world that the listeners should be added to.
     */
    public void registerListeners(World world) {
        for (ChunkPos chunkPos : getSpannedChunks()) {
            ChunkEventListeners.listeners.add(world, chunkPos, this);
        }
    }

    /**
     * Removes all chunk event listeners that the multiblock contains.
     * @param world The world that the listeners should be removed from.
     */
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
