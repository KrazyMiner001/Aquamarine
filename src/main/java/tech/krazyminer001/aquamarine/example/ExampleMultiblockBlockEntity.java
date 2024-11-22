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

    public static final ShapeTemplate template;

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

    static {
        ShapeTemplate.Builder builder = new ShapeTemplate.Builder();
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                builder.add(new BlockPos(x, 0, z), SimpleMembers.STEEL_CASING);
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 1, z), SimpleMembers.STEEL_CASING);
                } else {
                    builder.add(new BlockPos(x, 1, z), SimpleMembers.TERRACOTTA);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 2, z), SimpleMembers.STEEL_CASING);
                } else if (x == -2 || x == 2 || z == 1 || z == 5) {
                    builder.add(new BlockPos(x, 2, z), SimpleMembers.TERRACOTTA);
                } else {
                    builder.add(new BlockPos(x, 2, z), SimpleMembers.GRAPHITE);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 3, z), SimpleMembers.STEEL_CASING);
                } else if (x == -2 || x == 2 || z == 1 || z == 5) {
                    builder.add(new BlockPos(x, 3, z), SimpleMembers.TERRACOTTA);
                } else {
                    builder.add(new BlockPos(x, 3, z), SimpleMembers.AIR);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 4, z), SimpleMembers.STEEL_CASING);
                } else if (x == -2 || x == 2 || z == 1 || z == 5) {
                    builder.add(new BlockPos(x, 4, z), SimpleMembers.TERRACOTTA);
                } else {
                    builder.add(new BlockPos(x, 4, z), SimpleMembers.AIR);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 5, z), SimpleMembers.STEEL_CASING);
                } else if (x == -2 || x == 2 || z == 1 || z == 5) {
                    builder.add(new BlockPos(x, 5, z), SimpleMembers.TERRACOTTA);
                } else {
                    builder.add(new BlockPos(x, 5, z), SimpleMembers.COKE);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z < 7; z++) {
                if (x == -3 || x == 3 || z == 0 || z == 6) {
                    builder.add(new BlockPos(x, 6, z), SimpleMembers.STEEL_CASING);
                } else if (x == -2 || x == 2 || z == 1 || z == 5) {
                    builder.add(new BlockPos(x, 6, z), SimpleMembers.TERRACOTTA);
                }
            }
        }
        builder
                .add(new BlockPos(-1, 6, 2), SimpleMembers.COKE)
                .add(new BlockPos(-1, 6, 3), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(-1, 6, 4), SimpleMembers.COKE)
                .add(new BlockPos(0, 6, 2), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(0, 6, 3), SimpleMembers.COKE)
                .add(new BlockPos(0, 6, 4), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(1, 6, 2), SimpleMembers.COKE)
                .add(new BlockPos(1, 6, 3), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(1, 6, 4), SimpleMembers.COKE);
        builder
                .add(new BlockPos(-1,  7, 3), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(0,  7, 2), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(0,  7, 3), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(0,  7, 4), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(1,  7, 3), SimpleMembers.STEEL_CASING)
                .add(new BlockPos(0,  8, 3), SimpleMembers.STEEL_CASING);
        template = builder.build();
    }
}
