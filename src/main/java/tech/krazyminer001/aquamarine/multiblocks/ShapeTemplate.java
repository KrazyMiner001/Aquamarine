package tech.krazyminer001.aquamarine.multiblocks;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class describing the shape of a multiblock.
 */
public class ShapeTemplate {
    public static final PacketCodec<ByteBuf, ShapeTemplate> PACKET_CODEC = new PacketCodec<>() {

        @Override
        public void encode(ByteBuf buf, ShapeTemplate value) {
            PacketCodecs.map(
                    HashMap::new,
                    BlockPos.PACKET_CODEC,
                    SimpleMember.PACKET_CODEC
            ).encode(buf, (HashMap<BlockPos, SimpleMember>) value.simpleMembers);

            PacketCodecs.map(
                    HashMap::new,
                    BlockPos.PACKET_CODEC,
                    HatchFlags.PACKET_CODEC
            ).encode(buf, (HashMap<BlockPos, HatchFlags>) value.hatchFlags);
        }

        @Override
        public ShapeTemplate decode(ByteBuf buf) {
              HashMap<BlockPos, SimpleMember> simpleMembers = PacketCodecs.map(
                    HashMap::new,
                    BlockPos.PACKET_CODEC,
                    SimpleMember.PACKET_CODEC
              ).decode(buf);

              HashMap<BlockPos, HatchFlags> hatchFlags = PacketCodecs.map(
                      HashMap::new,
                      BlockPos.PACKET_CODEC,
                      HatchFlags.PACKET_CODEC
              ).decode(buf);

              return new ShapeTemplate(simpleMembers, hatchFlags);
        }
    };

    public final Map<BlockPos, SimpleMember> simpleMembers = new HashMap<>();
    public final Map<BlockPos, HatchFlags> hatchFlags = new HashMap<>();

    private ShapeTemplate() {}

    private ShapeTemplate(Map<BlockPos, SimpleMember> simpleMembers, Map<BlockPos, HatchFlags> hatchFlags) {
        this.simpleMembers.putAll(simpleMembers);
        this.hatchFlags.putAll(hatchFlags);
    }

    public void placeMultiblock(World world, BlockPos pos, Direction direction) {
        if (world.isClient()) return;
        Map<BlockPos, SimpleMember> members = ShapeMatcher.toWorldPos(pos, direction, simpleMembers);
        members.forEach((key, value) -> world.setBlockState(key, value.getPreviewState()));
    }

    public Map<BlockPos, SimpleMember> getSimpleMembers() {
        return simpleMembers;
    }

    public Map<BlockPos, HatchFlags> getHatchFlags() {
        return hatchFlags;
    }

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
