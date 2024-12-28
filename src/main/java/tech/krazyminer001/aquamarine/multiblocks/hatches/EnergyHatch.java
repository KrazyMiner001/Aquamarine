package tech.krazyminer001.aquamarine.multiblocks.hatches;


import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.HatchType;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableEnergyStack;

import java.util.ArrayList;
import java.util.List;

public class EnergyHatch extends HatchBlockEntity {
    private final boolean input;
    private final AquaInventory inventory;


    public EnergyHatch(BEP bep, boolean input, long capacity) {
        super(bep.blockEntityType(), bep.pos(), bep.state());
        this.input = input;
        this.inventory = new AquaInventory.Builder().addEnergyStack(new ConfigurableEnergyStack(capacity)).build();

    }

    public EnergyHatch(BEP bep, boolean input, long capacity, long amount) {
        super(bep.blockEntityType(), bep.pos(), bep.state());
        this.input = input;
        this.inventory = new AquaInventory.Builder().addEnergyStack(new ConfigurableEnergyStack(capacity, amount)).build();
    }

    @Override
    public HatchType getHatchType() {
        return input ? HatchType.TR_ENERGY_INPUT : HatchType.TR_ENERGY_OUTPUT;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        ArrayList<Long> capacities = new ArrayList<>();
        ArrayList<Long> amounts = new ArrayList<>();

        for (ConfigurableEnergyStack stack : inventory.getEnergy()) {
            capacities.add(stack.getResource().getCapacity());
            amounts.add(stack.getAmount());
        }

        nbt.putLongArray("capacities", capacities);
        nbt.putLongArray("amounts", amounts);

        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);

        long[] capacities = nbt.getLongArray("capacities");
        long[] amounts = nbt.getLongArray("amounts");

        ArrayList<ConfigurableEnergyStack> stacks = new ArrayList<>();

        for (int i = 0; i < capacities.length; i++) {
            stacks.add(new ConfigurableEnergyStack(capacities[i], amounts[i]));
        }

        this.inventory.getEnergyStorage().storage.clear();
        this.inventory.getEnergyStorage().storage.addAll(stacks);
    }

    public AquaInventory getInventory() {
        return inventory;
    }

    @Override
    public void appendEnergyInputs(List<ConfigurableEnergyStack> list) {
        if (input) list.addAll(inventory.getEnergy());
    }

    @Override
    public void appendEnergyOutputs(List<ConfigurableEnergyStack> list) {
        if (!input) list.addAll(inventory.getEnergy());
    }
}
