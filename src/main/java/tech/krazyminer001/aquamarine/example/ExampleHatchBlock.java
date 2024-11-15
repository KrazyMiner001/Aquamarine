package tech.krazyminer001.aquamarine.example;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.aquamarine.multiblocks.inventory.FluidStack;

import java.util.Objects;

public class ExampleHatchBlock extends BlockWithEntity {
    private static final MapCodec<ExampleHatchBlock> CODEC = createCodec(ExampleHatchBlock::new);

    protected ExampleHatchBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExampleHatchBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        FluidStack inserted = ((ExampleHatchBlockEntity) world.getBlockEntity(pos)).getInventory().getFluidStorage().fluidHandler.insertStack(0, new FluidStack(FluidVariant.of(Fluids.WATER), 20), false);
        Objects.requireNonNull(world.getBlockEntity(pos)).markDirty();

        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ExampleBlocks.HATCH_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick());
    }
}
