package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;

import java.util.Objects;

public interface EnergyVariant extends TransferVariant<EnergyStack> {
    static EnergyVariant blank() {
        return of(EnergyStack.BLANK);
    }

    static EnergyVariant of(EnergyStack energyStack) {
        return EnergyVariantImpl.of(energyStack);
    }

    static EnergyVariant of(long capacity) {
        return of(new EnergyStack(capacity));
    }

    long getCapacity();

    class EnergyVariantImpl implements EnergyVariant {
        private EnergyStack energyStack = null;

        private EnergyVariantImpl(EnergyStack energyStack) {
            this.energyStack = energyStack;
        }

        public static EnergyVariantImpl of(EnergyStack energyStack) {
            return new EnergyVariantImpl(energyStack);
        }

        @Override
        public long getCapacity() {
            return energyStack.getCapacity();
        }

        @Override
        public boolean isBlank() {
            if (energyStack == null) return true;
            return energyStack.isBlank();
        }

        @Override
        public EnergyStack getObject() {
            return energyStack;
        }

        @Override
        public ComponentChanges getComponents() {
            return null;
        }

        @Override
        public ComponentMap getComponentMap() {
            return null;
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof EnergyVariantImpl that)) return false;

            return this.energyStack.getCapacity() == that.energyStack.getCapacity();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(energyStack);
        }
    }
}