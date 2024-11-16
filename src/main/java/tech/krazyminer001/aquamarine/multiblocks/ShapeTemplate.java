package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ShapeTemplate {
    public final Map<BlockPos, SimpleMember> simpleMembers = new HashMap<>();
    public final Map<BlockPos, HatchFlags> hatchFlags = new HashMap<>();

    /**
     * Class describing the shape of a multiblock.
     */
    private ShapeTemplate() {}

    public static class Builder {
        private final ShapeTemplate template;

        /**
         * Creates a builder to make a {@link ShapeTemplate}.
         */
        public Builder() {
            template = new ShapeTemplate();
        }

        /**
         * Adds a position to the template.
         * @param pos The position to be added.
         * @param member {@link SimpleMember} that describes what blocks can be at that position.
         * @param hatchFlags {@link HatchFlags} that describes what hatches can replace the simple member.
         */
        public Builder add(BlockPos pos, SimpleMember member, @Nullable HatchFlags hatchFlags) {
            template.simpleMembers.put(pos, member);
            if (hatchFlags != null) {
                template.hatchFlags.put(pos, hatchFlags);
            }

            return this;
        }

        /**
         * Adds a position to the template.
         * @param x The x coordinate of the position to be added.
         * @param y The y coordinate of the position to be added.
         * @param z The z coordinate of the position to be added.
         * @param member {@link SimpleMember} that describes what blocks can be at that position.
         * @param hatchFlags {@link HatchFlags} that describes what hatches can replace the simple member.
         */
        public Builder add(int x, int y, int z, SimpleMember member, @Nullable HatchFlags hatchFlags) {
            return add(new BlockPos(x, y, z), member, hatchFlags);
        }

        /**
         * Adds a position to the template.
         * @param pos The position to be added.
         * @param member {@link SimpleMember} that describes what blocks can be at that position.
         */
        public Builder add(BlockPos pos, SimpleMember member) {
            return add(pos, member, null);
        }

        /**
         * Adds a position to the template.
         * @param x The x coordinate of the position to be added.
         * @param y The y coordinate of the position to be added.
         * @param z The z coordinate of the position to be added.
         * @param member {@link SimpleMember} that describes what blocks can be at that position.
         */
        public Builder add(int x, int y, int z, SimpleMember member) {
            return add(x, y, z, member, null);
        }

        /**
         * Removes a position from the template.
         * @param x The x coordinate of the position to be removed.
         * @param y The y coordinate of the position to be removed.
         * @param z The z coordinate of the position to be removed.
         */
        public Builder remove(int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            template.simpleMembers.remove(pos);
            template.hatchFlags.remove(pos);
            return this;
        }

        /**
         * Creates a {@link ShapeTemplate} instance based on builder data.
         * @return The {@link ShapeTemplate} instance.
         */
        public ShapeTemplate build() {
            remove(0, 0, 0);
            return template;
        }

    }

}
