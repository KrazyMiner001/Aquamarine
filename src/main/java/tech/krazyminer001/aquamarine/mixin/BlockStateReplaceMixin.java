package tech.krazyminer001.aquamarine.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.krazyminer001.aquamarine.events.BlockStateReplacedCallback;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class BlockStateReplaceMixin {
    @Inject(at = @At("TAIL"), method = "onStateReplaced")
    private void onStateReplaced(World world, BlockPos pos, BlockState state, boolean moved, CallbackInfo ci) {
        BlockStateReplacedCallback.EVENT.invoker().onBlockStateReplaced(world, pos, state, moved);
    }

}
