package tech.krazyminer001.aquamarine.multiblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import tech.krazyminer001.aquamarine.blocks.FastBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory;

public abstract class MultiblockBlockEntity extends FastBlockEntity {


    public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ShapeTemplate getShapeTemplate();

    public abstract MultiblockInventory getMultiblockInventory();

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, MultiblockBlockEntity entity) {
        if (world.isClient()) return;
        entity.tick();
    }

    protected void tick() {}
}
