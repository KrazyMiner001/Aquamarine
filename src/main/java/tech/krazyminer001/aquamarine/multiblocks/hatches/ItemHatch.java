package tech.krazyminer001.aquamarine.multiblocks.hatches;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.aquamarine.multiblocks.BEP;
import tech.krazyminer001.aquamarine.multiblocks.HatchBlockEntity;
import tech.krazyminer001.aquamarine.multiblocks.HatchType;
import tech.krazyminer001.aquamarine.multiblocks.inventory.AquaInventory;

public class ItemHatch extends HatchBlockEntity {
    private final boolean input;
    private final AquaInventory inventory;

    public ItemHatch(BEP bep, boolean input, AquaInventory inventory) {
        super(bep.blockEntityType(), bep.pos(), bep.state());
        this.input = input;
        this.inventory = inventory;
    }

    @Override
    public HatchType getHatchType() {
        return input ? HatchType.ITEM_INPUT : HatchType.ITEM_OUTPUT;
    }

    @Override
    protected void tickTransfer() {
        if  (input) {

        } else {

        }
    }

    public AquaInventory getInventory() {
        return inventory;
    }
}
