package tech.krazyminer001.aquamarine.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import tech.krazyminer001.aquamarine.multiblocks.*;

import java.awt.*;
import java.util.Objects;

public class HatchBlockOutlineRenderer {
    public static void renderHatchBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        if (blockOutlineContext.entity() instanceof PlayerEntity player) {
            World world = worldRenderContext.world();
            BlockPos pos = blockOutlineContext.blockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MultiblockBlockEntity multiblockBlockEntity) {
                if (player.getMainHandStack().getItem() instanceof BlockItem playerBlockItem) {
                    if (playerBlockItem.getBlock() instanceof BlockWithEntity playerItemBlockWithEntity) {
                        if (playerItemBlockWithEntity.createBlockEntity(BlockPos.ORIGIN, playerItemBlockWithEntity.getDefaultState()) instanceof HatchBlockEntity hatchBlockEntity) {
                            HatchType hatchType = hatchBlockEntity.getHatchType();
                            ShapeTemplate template = multiblockBlockEntity.getShapeTemplate();
                            ShapeMatcher matcher = new ShapeMatcher(world, pos, world.getBlockState(pos).get(Properties.FACING), template);
                            matcher.rematch(world);
                            if (matcher.isMatchSuccessful())  {
                                for (BlockPos matchedPos : matcher.getPositions()) {
                                    if (!(world.getBlockEntity(pos) instanceof HatchBlockEntity)) {
                                        if (matcher.getHatchFlags(matchedPos).allows(hatchType)) {
                                            MatrixStack matrixStack = Objects.requireNonNull(worldRenderContext.matrixStack());

                                            matrixStack.push();
                                            VertexConsumerProvider vertexConsumerProvider = worldRenderContext.consumers();

                                            Color color = new Color(0xFF55AF53, true);

                                            BlockState state = world.getBlockState(matchedPos);

                                            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugFilledBox());

                                            Vec3d cameraPos = worldRenderContext.camera().getPos();

                                            state.getOutlineShape(world, pos, ShapeContext.of(player)).forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) -> {
                                                Box box = new Box(minX, minY, minZ, maxX, maxY, maxZ)
                                                        .offset(new Vec3d(matchedPos.getX(), matchedPos.getY(), matchedPos.getZ()).subtract(cameraPos))
                                                                .expand(0.03125);

                                                VertexRendering.drawFilledBox(
                                                        matrixStack,
                                                        vertexConsumer,
                                                        box.minX,
                                                        box.minY,
                                                        box.minZ,
                                                        box.maxX,
                                                        box.maxY,
                                                        box.maxZ,
                                                        (float) color.getRed() / 255,
                                                        (float) color.getGreen() / 255,
                                                        (float) color.getBlue() / 255,
                                                        (float) color.getAlpha() / 255
                                                );
                                            });

                                            matrixStack.pop();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
