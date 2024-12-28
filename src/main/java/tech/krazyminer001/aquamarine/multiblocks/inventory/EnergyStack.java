package tech.krazyminer001.aquamarine.multiblocks.inventory;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public class EnergyStack implements EnergyStorage {
    private long energy;
    private final long capacity;

    public static final EnergyStack BLANK = new EnergyStack(0, 0);

    public EnergyStack(long capacity) {
        this.capacity = capacity;
        this.energy = 0;
    }

    public EnergyStack(long capacity, long energy) {
        this.capacity = capacity;
        this.energy = energy;
    }

    public boolean isBlank() {
        return energy == 0;
    }

    @Override
    public long insert(long toAdd, TransactionContext transactionContext) {
        long oldEnergy = energy;
        energy = Math.min(energy + toAdd, capacity);
        return energy - oldEnergy;
    }

    @Override
    public long extract(long toExtract, TransactionContext transactionContext) {
        long oldEnergy = energy;
        energy = Math.max(energy - toExtract, 0);
        return oldEnergy - energy;
    }

    @Override
    public long getAmount() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EnergyStack that)) return false;

        return energy == that.energy && capacity == that.capacity;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(energy);
        result = 31 * result + Long.hashCode(capacity);
        return result;
    }
}
