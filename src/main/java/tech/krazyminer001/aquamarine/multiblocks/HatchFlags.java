package tech.krazyminer001.aquamarine.multiblocks;

import java.util.HashSet;
import java.util.Set;

public class HatchFlags {
    private final Set<HatchType> flags;
    public static final HatchFlags EMPTY = new HatchFlags(Set.of());

    public HatchFlags(Set<HatchType> flags) {
        this.flags = flags;
    }

    public boolean allows(HatchType type) {
        return flags.contains(type);
    }

    public static class Builder {
        private final Set<HatchType> flags = new HashSet<>();

        public Builder with(HatchType type) {
            flags.add(type);
            return this;
        }

        public Builder with(HatchType... types) {
            for (HatchType type : types) {
                with(type);
            }
            return this;
        }

        public HatchFlags build() {
            return new HatchFlags(flags);
        }
    }
}
