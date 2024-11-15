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
import tech.krazyminer001.aquamarine.multiblocks.*;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableItemStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExampleMultiblockBlockEntity extends FastBlockEntity {
    private final List<HatchBlockEntity> hatches = new ArrayList<>();
    private final MultiblockInventory multiblockInventory = new MultiblockInventory();

    public ExampleMultiblockBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleBlocks.EXAMPLE_MULTIBLOCK_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, pos, state);
    }

    private AquaInventory inventory = new AquaInventory(
            List.of(new ConfigurableItemStack()),
            List.of()
    );

    private static final ShapeTemplate template = new ShapeTemplate.Builder()
            .add(0, 1,  0, SimpleMember.ofBlock(() -> Blocks.GLASS), new HatchFlags.Builder().with(HatchType.ITEM_INPUT).build())
            .build();

    private ShapeMatcher matcher = null;

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, ExampleMultiblockBlockEntity entity) {
        if (world.isClient()) return;
        entity.tick();
    }

    private void tick() {
        assert world != null;
        if (world.isClient()) return;
        link();

        if (hatches != matcher.getMatchedHatches()) {
            hatches.clear();
            hatches.addAll(matcher.getMatchedHatches());
        }

        Aquamarine.LOGGER.info("Multiblock does{} match", matcher.isMatchSuccessful() ? "" : "n't");

        Aquamarine.LOGGER.info("Multiblock has {} hatches", hatches.size());
        if (!multiblockInventory.getItemInputs().isEmpty())
            Aquamarine.LOGGER.info("Multiblock has {} {}s", multiblockInventory.getItemInputs().getFirst().getAmount(), multiblockInventory.getItemInputs().getFirst().getResource().getItem());
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

        if (matcher.isMatchSuccessful()) {
            multiblockInventory.recollectIO(matcher);
        }
    }
}
