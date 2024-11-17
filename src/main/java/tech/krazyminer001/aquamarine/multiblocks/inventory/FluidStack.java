package tech.krazyminer001.aquamarine.multiblocks.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

/**
 * Fluid alternative to {@link net.minecraft.item.ItemStack}.
 */
public class FluidStack
{
    public static final FluidStack EMPTY = new FluidStack(FluidVariant.blank(), 0);

    public FluidVariant fluidVariant;
    public long amount;

    @SuppressWarnings( "deprecation" )
    public static final Codec<RegistryEntry<Fluid>> FLUID_ENTRY_CODEC = Registries.FLUID
            .getEntryCodec()
            .validate(fluidRegistryEntry -> fluidRegistryEntry.matches(Fluids.EMPTY.getRegistryEntry()) ? DataResult.error(() -> "Fluid must not be empty") : DataResult.success(fluidRegistryEntry));

    public static final Codec<FluidStack> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                            FLUID_ENTRY_CODEC.fieldOf("id").forGetter(FluidStack::getRegistryEntry),
                            Codec.LONG.fieldOf("count").orElse(1l).forGetter(FluidStack::getAmount)
                    )
                            .apply(instance, FluidStack::new)
            )
    );

    public FluidStack(FluidVariant variant, long amount)
    {
        this.fluidVariant = variant;
        this.amount = amount;
    }

    public FluidStack(RegistryEntry<Fluid> fluidEntry, long amount) {
        this(FluidVariant.of(fluidEntry.value()), amount);
    }

    @SuppressWarnings("deprecation")
    public RegistryEntry<Fluid> getRegistryEntry() {
        return this.fluidVariant.getFluid().getRegistryEntry();
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    public FluidVariant getFluidVariant()
    {
        return fluidVariant;
    }

    public void setFluidVariant(FluidVariant variant)
    {
        this.fluidVariant = variant;
    }

    public long getAmount()
    {
        return amount;
    }

    public void setAmount(long amount)
    {
        this.amount = amount;
    }

    /**
     * Creates nbt from the stack.
     */
    public NbtElement toNbt(RegistryWrapper.WrapperLookup registries, NbtElement prefix) {
        return CODEC.encode(this, registries.getOps(NbtOps.INSTANCE), prefix).getOrThrow();
    }

    /**
     * Creates a stack from nbt.
     */
    public static Optional<FluidStack> fromNbt(RegistryWrapper.WrapperLookup registries, NbtElement prefix) {
        return CODEC.parse(registries.getOps(NbtOps.INSTANCE), prefix).resultOrPartial();
    }
}