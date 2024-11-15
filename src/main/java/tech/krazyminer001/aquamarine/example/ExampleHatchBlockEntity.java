package tech.krazyminer001.aquamarine.example;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.Aquamarine;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.hatches.FluidHatch;
import tech.krazyminer001.aquamarine.multiblocks.hatches.ItemHatch;
import tech.krazyminer001.aquamarine.multiblocks.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class ExampleHatchBlockEntity extends FluidHatch implements BlockEntityFluidInventory {
    public ExampleHatchBlockEntity(BlockPos pos, BlockState state) {
        super(new BEP(ExampleBlocks.HATCH_BLOCK_ENTITY, pos, state), true, List.of(new ConfigurableFluidStack(1000)));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AquaInventory getAquaInventory() {
        return getInventory();
    }
}
