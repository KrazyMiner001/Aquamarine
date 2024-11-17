package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.ShapeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MultiblockInventory {
    private final List<ConfigurableItemStack> itemInputs = new ArrayList<>();
    private final List<ConfigurableItemStack> itemOutputs = new ArrayList<>();
    private final List<ConfigurableFluidStack> fluidInputs = new ArrayList<>();
    private final List<ConfigurableFluidStack> fluidOutputs = new ArrayList<>();

    private int invHash = 0;

    public void recollectIO(ShapeMatcher matcher) {
        List<HatchBlockEntity> hatches = new ArrayList<>(matcher.getMatchedHatches());

        recollectList(hatches, itemInputs, HatchBlockEntity::appendItemInputs);
        recollectList(hatches, itemOutputs, HatchBlockEntity::appendItemOutputs);
        recollectList(hatches, fluidInputs, HatchBlockEntity::appendFluidInputs);
        recollectList(hatches, fluidOutputs, HatchBlockEntity::appendFluidOutputs);
    }

    private <T, Stack extends ConfigurableStack<T, ? extends TransferVariant<T>>> void recollectList(
            List<HatchBlockEntity> hatches, List<Stack> stacks, BiConsumer<HatchBlockEntity, List<Stack>> consumer) {
        // Clear old IO
        for (Stack stack : stacks) {
            stack.removeListener(listener);
        }

        stacks.clear();

        for (HatchBlockEntity hatch : hatches) {
            consumer.accept(hatch, stacks);
        }

        listener.listenAll(stacks, null);
    }

    private final ChangeListener listener = new ChangeListener() {
        @Override
        protected void onChange() {
            ++invHash;
        }

        @Override
        protected boolean isValid(Object token) {
            return true;
        }
    };

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof MultiblockInventory that)) return false;

        return invHash == that.invHash && itemInputs.equals(that.itemInputs) && itemOutputs.equals(that.itemOutputs) && fluidInputs.equals(that.fluidInputs) && fluidOutputs.equals(that.fluidOutputs);
    }

    @Override
    public int hashCode() {
        int result = itemInputs.hashCode();
        result = 31 * result + itemOutputs.hashCode();
        result = 31 * result + fluidInputs.hashCode();
        result = 31 * result + fluidOutputs.hashCode();
        result = 31 * result + invHash;
        return result;
    }

    public List<ConfigurableItemStack> getItemInputs() {
        return itemInputs;
    }

    public List<ConfigurableItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public List<ConfigurableFluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public List<ConfigurableFluidStack> getFluidOutputs() {
        return fluidOutputs;
    }
}
