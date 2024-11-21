package tech.krazyminer001.aquamarine.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.Aquamarine;
import tech.krazyminer001.aquamarine.multiblocks.*;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableFluidStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableItemStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.MultiblockInventory;

import java.util.List;

public class ExampleMultiblockBlockEntity extends MultiblockBlockEntity {
    private final MultiblockInventory multiblockInventory = new MultiblockInventory();

    public ExampleMultiblockBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleBlocks.EXAMPLE_MULTIBLOCK_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, pos, state);
    }

    private final AquaInventory inventory = new AquaInventory(
            List.of(new ConfigurableItemStack()),
            List.of(new ConfigurableFluidStack(1000))
    );

    private static final ShapeTemplate template = new ShapeTemplate.Builder()
            .add(0, 1,  0, SimpleMember.ofBlock(() -> Blocks.GLASS), new HatchFlags.Builder().with(HatchType.FLUID_INPUT).build())
            .build();

    @Override
    public MultiblockInventory getMultiblockInventory() {
        return multiblockInventory;
    }

    @Override
    public ShapeTemplate getShapeTemplate() {
        return template;
    }

    @Override
    protected void tick() {
        super.tick();

        //Aquamarine.LOGGER.info("Multiblock does{} match", matcher.isMatchSuccessful() ? "" : "n't");

        //Aquamarine.LOGGER.info("Multiblock has {} hatches", hatches.size());
        if (!multiblockInventory.getFluidInputs().isEmpty())
            Aquamarine.LOGGER.info("Multiblock has {} {}s", multiblockInventory.getFluidInputs().getFirst().getAmount(), multiblockInventory.getFluidInputs().getFirst().getResource().getFluid());
    }
}
