package tech.krazyminer001.aquamarine.example;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import tech.krazyminer001.aquamarine.Aquamarine;

import java.util.Optional;

public class ExampleBlocks {
    public static final Block EXAMPLE_MULTIBLOCK = register("example_multiblock",
            new ExampleMultiblock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Aquamarine.MOD_ID, "example_multiblock")))), true);

    public static final BlockEntityType<ExampleMultiblockBlockEntity> EXAMPLE_MULTIBLOCK_BLOCK_ENTITY_BLOCK_ENTITY_TYPE = register("example_block_entity",
            FabricBlockEntityTypeBuilder.create(ExampleMultiblockBlockEntity::new, EXAMPLE_MULTIBLOCK).build()
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Aquamarine.MOD_ID, id), type);
    }

    private static <T extends Block> T register(String name, T block, boolean registerItem) {
        if (registerItem) {
            Registry.register(Registries.ITEM, Identifier.of(Aquamarine.MOD_ID, name), new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aquamarine.MOD_ID, "example_multiblock")))));
        }
        return Registry.register(Registries.BLOCK, Identifier.of(Aquamarine.MOD_ID, name), block);
    }

    public static void init() {}
}
