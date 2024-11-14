package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ShapeTemplate {
    public final Map<BlockPos, SimpleMember> simpleMembers = new HashMap<>();
    public final Map<BlockPos, HatchFlags> hatchFlags = new HashMap<>();

    private ShapeTemplate() {}

    public static class Builder {
        private final ShapeTemplate template;

        public Builder() {
            template = new ShapeTemplate();
        }

        public Builder add(BlockPos pos, SimpleMember member, @Nullable HatchFlags hatchFlags) {
            template.simpleMembers.put(pos, member);
            if (hatchFlags != null) {
                template.hatchFlags.put(pos, hatchFlags);
            }

            return this;
        }

        public Builder add(int x, int y, int z, SimpleMember member, @Nullable HatchFlags hatchFlags) {
            return add(new BlockPos(x, y, z), member, hatchFlags);
        }

        public Builder add(BlockPos pos, SimpleMember member) {
            return add(pos, member, null);
        }

        public Builder add(int x, int y, int z, SimpleMember member) {
            return add(x, y, z, member, null);
        }

        public Builder remove(int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            template.simpleMembers.remove(pos);
            template.hatchFlags.remove(pos);
            return this;
        }

        public ShapeTemplate build() {
            remove(0, 0, 0);
            return template;
        }

    }

}
