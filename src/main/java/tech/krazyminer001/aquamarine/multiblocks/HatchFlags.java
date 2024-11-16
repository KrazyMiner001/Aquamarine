package tech.krazyminer001.aquamarine.multiblocks;

import java.util.HashSet;
import java.util.Set;

public class HatchFlags {
    private final Set<HatchType> flags;
    public static final HatchFlags EMPTY = new HatchFlags(Set.of());

    /**
     * A collection of hatch types, indicating what hatches a multiblock member may have.
     * @param flags Set of hatch types that this flag allows.
     */
    public HatchFlags(Set<HatchType> flags) {
        this.flags = flags;
    }

    /**
     * @param type {@link HatchType} to test
     * @return Whether the {@link HatchType} is allowed by this flag.
     */
    public boolean allows(HatchType type) {
        return flags.contains(type);
    }

    public static class Builder {
        private final Set<HatchType> flags = new HashSet<>();

        /**
         * Adds extra hatch type to the flags.
         * @param type the type to be added.
         */
        public Builder with(HatchType type) {
            flags.add(type);
            return this;
        }

        /**
         * Adds multiple hatch types to the flags.
         * @param types the types to add.
         */
        public Builder with(HatchType... types) {
            for (HatchType type : types) {
                with(type);
            }
            return this;
        }

        /**
         * Turns the builder into a {@link HatchFlags} instance.
         * @return the {@link HatchFlags} instance;
         */
        public HatchFlags build() {
            return new HatchFlags(flags);
        }
    }
}
