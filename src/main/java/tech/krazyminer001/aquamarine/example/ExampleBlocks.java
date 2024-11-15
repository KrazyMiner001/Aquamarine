package tech.krazyminer001.aquamarine.example;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import tech.krazyminer001.aquamarine.Aquamarine;

import java.util.function.Function;

public class ExampleBlocks {
    public static final ExampleMultiblock EXAMPLE_MULTIBLOCK = register("example_multiblock",
            ExampleMultiblock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), true);

    public static final ExampleHatchBlock EXAMPLE_HATCH_BLOCK = register("example_hatch_block",
            ExampleHatchBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), true);

    public static final BlockEntityType<ExampleMultiblockBlockEntity> EXAMPLE_MULTIBLOCK_BLOCK_ENTITY_BLOCK_ENTITY_TYPE = register("example_block_entity",
            FabricBlockEntityTypeBuilder.create(ExampleMultiblockBlockEntity::new, EXAMPLE_HATCH_BLOCK).build()
    );

    public static final BlockEntityType<ExampleHatchBlockEntity> HATCH_BLOCK_ENTITY = register("example_hatch",
            FabricBlockEntityTypeBuilder.create(ExampleHatchBlockEntity::new, EXAMPLE_MULTIBLOCK).build());

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Aquamarine.MOD_ID, id), type);
    }

    private static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> blockFunction, AbstractBlock.Settings blockSettings, boolean registerItem) {
        Identifier identifier = Identifier.of(Aquamarine.MOD_ID, name);
        RegistryKey<Block> blockRegistryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        T block = Registry.register(Registries.BLOCK, identifier, blockFunction.apply(blockSettings.registryKey(blockRegistryKey)));
        if (registerItem) {
            RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, identifier);
            Registry.register(Registries.ITEM, identifier, new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(itemRegistryKey)));
        }
        return block;
    }

    public static void init() {}
}
