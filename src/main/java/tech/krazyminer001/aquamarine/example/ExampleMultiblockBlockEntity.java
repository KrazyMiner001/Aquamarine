package tech.krazyminer001.aquamarine.example;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import tech.krazyminer001.aquamarine.Aquamarine;
import tech.krazyminer001.aquamarine.blocks.FastBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.ShapeMatcher;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;
import tech.krazyminer001.aquamarine.multiblocks.SimpleMember;

import java.util.function.Supplier;

public class ExampleMultiblockBlockEntity extends FastBlockEntity {
    public ExampleMultiblockBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleBlocks.EXAMPLE_MULTIBLOCK_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, pos, state);
    }

    private static final ShapeTemplate template = new ShapeTemplate.Builder()
            .add(0, 1,  0, SimpleMember.ofBlock(() -> Blocks.GLASS))
            .build();
    private ShapeMatcher matcher = null;

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, ExampleMultiblockBlockEntity entity) {
        if (world.isClient()) return;
        entity.link();

        Aquamarine.LOGGER.info("Multiblock does{} match", entity.matcher.isMatchSuccessful() ? "" : "n't");
    }

    public final void link() {
        assert world != null;
        if (matcher == null) {
            matcher = new ShapeMatcher(world, pos, world.getBlockState(pos).get(ExampleMultiblock.FACING), template);
            matcher.registerListeners(world);
        }

        if (matcher.needsRematch()) {
            matcher.rematch(world);
        }
    }
}
