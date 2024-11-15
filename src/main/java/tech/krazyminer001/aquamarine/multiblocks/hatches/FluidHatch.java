package tech.krazyminer001.aquamarine.multiblocks.hatches;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.HatchType;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableFluidStack;
import tech.krazyminer001.aquamarine.multiblocks.inventory.FluidStack;

import java.util.List;

public class FluidHatch extends HatchBlockEntity {
    private final boolean input;
    private final AquaInventory inventory;

    public FluidHatch(BEP bep, boolean input, List<ConfigurableFluidStack> stacks) {
        super(bep.blockEntityType(), bep.pos(), bep.state());
        this.input = input;
        inventory = new AquaInventory(List.of(), stacks);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        NbtList nbtList = new NbtList();

        for (int i = 0; i < inventory.getFluids().size(); i++) {
            FluidStack fluidStack = inventory.getFluids().get(i).toStack();
            if (!fluidStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte) i);
                nbtList.add(fluidStack.toNbt(registries, nbtCompound));
            }
        }

        if (!nbtList.isEmpty()) {
            nbt.put("Fluids", nbtList);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList nbtList = nbt.getList("Fluids", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int slot = nbtCompound.getByte("Slot") & 0xff;
            if (slot < inventory.getFluids().size()) {
                inventory.getFluidStorage().fluidHandler.setStack(slot, FluidStack.fromNbt(registries, nbtCompound).orElse(FluidStack.EMPTY));
            }
        }

        super.readNbt(nbt, registries);
    }

    @Override
    public void appendFluidInputs(List<ConfigurableFluidStack> list) {
        if (input) list.addAll(inventory.getFluids());
    }

    @Override
    public void appendFluidOutputs(List<ConfigurableFluidStack> list) {
        if (!input) list.addAll(inventory.getFluids());
    }

    @Override
    public HatchType getHatchType() {
        return input ? HatchType.FLUID_INPUT : HatchType.FLUID_OUTPUT;
    }

    public AquaInventory getInventory() {
        return inventory;
    }
}
