package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import tech.krazyminer001.aquamarine.blocks.FastBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiblockBlockEntity extends FastBlockEntity {
    private ShapeMatcher matcher = null;
    private final List<HatchBlockEntity> hatches = new ArrayList<>();

    public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ShapeTemplate getShapeTemplate();

    public abstract MultiblockInventory getMultiblockInventory();

    protected ShapeMatcher getShapeMatcher() {return matcher;}

    protected void setShapeMatcher(ShapeMatcher matcher) {
        this.matcher = matcher;
    }

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, MultiblockBlockEntity entity) {
        if (world.isClient()) return;
        entity.tick();
    }

    public final void link() {
        assert world != null;
        if (matcher == null) {
            matcher = new ShapeMatcher(world, pos, world.getBlockState(pos).get(Properties.FACING), getShapeTemplate());
            matcher.registerListeners(world);
        }

        if (matcher.needsRematch()) {
            matcher.rematch(world);
        }

        if (matcher.isMatchSuccessful()) {
            getMultiblockInventory().recollectIO(matcher);
        }
    }

    protected void tick() {
        assert world != null;
        if (world.isClient()) return;
        link();

        if (hatches != matcher.getMatchedHatches()) {
            hatches.clear();
            hatches.addAll(matcher.getMatchedHatches());
        }
    }
}
