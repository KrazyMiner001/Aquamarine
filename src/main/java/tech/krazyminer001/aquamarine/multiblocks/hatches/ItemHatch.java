package tech.krazyminer001.aquamarine.multiblocks.hatches;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.HatchType;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;
import tech.krazyminer001.aquamarine.multiblocks.inventory.ConfigurableItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemHatch extends HatchBlockEntity {
    private final boolean input;
    private final AquaInventory inventory;

    public ItemHatch(BEP bep, boolean input, List<ConfigurableItemStack> slots) {
        super(bep.blockEntityType(), bep.pos(), bep.state());
        this.input = input;
        this.inventory = new AquaInventory(slots, List.of());
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        DefaultedList<ItemStack> items = DefaultedList.of();

        items.addAll(inventory.getItems().stream().map((configurableItemStack -> configurableItemStack.getResource().toStack((int) configurableItemStack.getAmount()))).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        Inventories.writeNbt(nbt, items, registries);
        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        DefaultedList<ItemStack> items = DefaultedList.of();

        Inventories.readNbt(nbt, items, registries);

        this.inventory.getItemStorage().storage.clear();

        this.inventory.getItemStorage().storage.addAll(items.stream()
                .map((itemStack -> new ConfigurableItemStack(ItemVariant.of(itemStack), itemStack.getCount())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    @Override
    public void appendItemInputs(List<ConfigurableItemStack> list) {
        if (input) list.addAll(inventory.getItems());
    }

    @Override
    public void appendItemOutputs(List<ConfigurableItemStack> list) {
        if (!input) list.addAll(inventory.getItems());
    }

    @Override
    public HatchType getHatchType() {
        return input ? HatchType.ITEM_INPUT : HatchType.ITEM_OUTPUT;
    }

    public AquaInventory getInventory() {
        return inventory;
    }
}
