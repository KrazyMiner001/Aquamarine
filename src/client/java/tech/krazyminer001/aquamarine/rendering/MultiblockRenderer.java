package tech.krazyminer001.aquamarine.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.aquamarine.Aquamarine;
import tech.krazyminer001.aquamarine.multiblocks.ShapeMatcher;
import tech.krazyminer001.aquamarine.multiblocks.ShapeTemplate;
import tech.krazyminer001.aquamarine.multiblocks.SimpleMember;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.SequencedMap;

public class MultiblockRenderer {
    public static boolean hasMulitblock;
    private static ShapeTemplate multiblockTemplate;
    private static BlockPos controllerPos;
    private static Direction controllerDirection;
    private static GhostVertexConsumers vertexConsumers;

    public static void setMultiblock(ShapeTemplate multiblock, BlockPos controllerPos, Direction controllerDirection) {
        multiblockTemplate = multiblock;
        MultiblockRenderer.controllerPos = controllerPos;
        MultiblockRenderer.controllerDirection = controllerDirection;
        hasMulitblock = multiblockTemplate != null;
    }

    public static void clearMultiblock() {
        multiblockTemplate = null;
        controllerPos = null;
        controllerDirection = null;
        hasMulitblock = false;
    }

    public static void onRenderEvent(MatrixStack matrixStack) {
        if (hasMulitblock && multiblockTemplate != null) {
            renderMultiblock(MinecraftClient.getInstance().world, matrixStack);
        }
    }

    private static void renderMultiblock(World world, MatrixStack matrixStack) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        Map<BlockPos, SimpleMember> members = ShapeMatcher.toWorldPos(controllerPos, controllerDirection, multiblockTemplate.simpleMembers);

        EntityRenderDispatcher entityRenderDispatcher = minecraftClient.getEntityRenderDispatcher();
        matrixStack.push();
        matrixStack.translate(entityRenderDispatcher.camera.getPos().multiply(-1));

        if (vertexConsumers == null) {
            vertexConsumers = remapVertexConsumers(MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
        }

        ShapeMatcher matcher = new ShapeMatcher(world, controllerPos, controllerDirection, multiblockTemplate);

        for (BlockPos pos : members.keySet()) {
            if (!matcher.matches(pos, world, null)) {
                renderBlock(world, members.get(pos).getPreviewState(), pos, matrixStack);
            }
        }

        vertexConsumers.draw();
        matrixStack.pop();
    }

    public static void renderBlock(World world, BlockState state, BlockPos pos, MatrixStack matrixStack) {
        if (pos != null) {
            matrixStack.push();
            matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());
        }

        float scale;

        if (state.isAir())  {
            scale = 0.375f;

            state = Blocks.RED_CONCRETE.getDefaultState();
        } else {
            scale = 0.875f;
        }

        float offset = (1f - scale) / 2;

        matrixStack.translate(offset, offset, offset);
        matrixStack.scale(scale, scale, scale);

        BlockRenderView view = getBlockRenderView(world, state);
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();

        manager.renderBlock(
                state,
                pos,
                view,
                matrixStack,
                vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                false,
                world.getRandom()
        );

        matrixStack.pop();
    }

    private static @NotNull BlockRenderView getBlockRenderView(World world, BlockState state) {
        return new BlockRenderView() {
            @Override
            public int getHeight() {
                return 255;
            }

            @Override
            public int getBottomY() {
                return 0;
            }

            @Override
            public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos1) {
                return state;
            }

            @Override
            public FluidState getFluidState(BlockPos pos) {
                return null;
            }

            @Override
            public float getBrightness(Direction direction, boolean shaded) {
                return 1;
            }

            @Override
            public LightingProvider getLightingProvider() {
                return LightingProvider.DEFAULT;
            }

            @Override
            public int getColor(BlockPos pos, ColorResolver colorResolver) {
                var plains = world.getRegistryManager().getOrThrow(RegistryKeys.BIOME)
                        .getOrThrow(BiomeKeys.PLAINS).value();
                return colorResolver.getColor(plains, pos.getX(), pos.getZ());
            }
        };
    }

    private static GhostVertexConsumers remapVertexConsumers(VertexConsumerProvider.Immediate original) {
        BufferAllocator allocator = original.allocator;
        SequencedMap<RenderLayer, BufferAllocator> layerBuffers = original.layerBuffers;

        SequencedMap<RenderLayer, BufferAllocator> remappedLayerBuffers = layerBuffers.entrySet()
                .stream()
                .map((entry) -> Map.entry(GhostRenderLayer.remap(entry.getKey()), entry.getValue()))
                .collect(
                        Object2ObjectLinkedOpenHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        Object2ObjectLinkedOpenHashMap::putAll);

        return new GhostVertexConsumers(allocator, remappedLayerBuffers);
    }

    private static class GhostVertexConsumers extends VertexConsumerProvider.Immediate {
        protected GhostVertexConsumers(BufferAllocator allocator, SequencedMap<RenderLayer, BufferAllocator> sequencedMap) {
            super(allocator, sequencedMap);
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer renderLayer) {
            return super.getBuffer(GhostRenderLayer.remap(renderLayer));
        }
    }

    private static class GhostRenderLayer extends RenderLayer {
        private static final Map<RenderLayer, GhostRenderLayer> remappedLayers = new IdentityHashMap<>();

        private GhostRenderLayer(RenderLayer original) {
            super(
                    original.toString() + "_" + Aquamarine.MOD_ID + "_ghost",
                    original.getVertexFormat(),
                    original.getDrawMode(),
                    original.getExpectedBufferSize(),
                    original.hasCrumbling(),
                    original.isTranslucent(),
                    () -> {
                        original.startDrawing();

                        RenderSystem.disableDepthTest();
                        RenderSystem.enableBlend();
                        RenderSystem.setShaderColor(20, 20, 20, 0.6f);
                    },
                    () -> {
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        RenderSystem.disableBlend();
                        RenderSystem.enableDepthTest();

                        original.endDrawing();
                    }
            );
        }

        public static GhostRenderLayer remap(RenderLayer original) {
            if (original instanceof GhostRenderLayer ghostRenderLayer) {
                return ghostRenderLayer;
            } else {
                return remappedLayers.computeIfAbsent(original, (layer) -> {
                    if (layer.toString().contains("cutout") && !layer.toString().contains("entity")) {
                        layer = RenderLayer.getTranslucent();
                    }
                    return new GhostRenderLayer(layer);
                });
            }
        }
    }
}
