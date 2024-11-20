package tech.krazyminer001.aquamarine.util;

import net.minecraft.util.Identifier;
import tech.krazyminer001.aquamarine.Aquamarine;

public class Utilities {
    public static Identifier of(String path) {
        return Identifier.of(Aquamarine.MOD_ID, path);
    }
}
