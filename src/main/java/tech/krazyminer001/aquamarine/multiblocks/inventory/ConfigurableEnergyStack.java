package tech.krazyminer001.aquamarine.multiblocks.inventory;

public class ConfigurableEnergyStack extends ConfigurableStack<EnergyStack, EnergyVariant> {
    public ConfigurableEnergyStack(long capacity) {
        super(EnergyVariant.of(capacity), 0);
    }

    public ConfigurableEnergyStack(long capacity, long amount) {
        super(EnergyVariant.of(capacity), amount);
    }

    @Override
    protected EnergyVariant getBlank() {
        return EnergyVariant.blank();
    }

    @Override
    protected long getRemainingCapacityFor(EnergyVariant resource) {
        return this.resource.getCapacity() - this.amount;
    }

    public EnergyStack toStack() {
        return new EnergyStack(this.resource.getCapacity(), this.amount);
    }
}
