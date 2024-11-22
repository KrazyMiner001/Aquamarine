package tech.krazyminer001.aquamarine.example;

import net.minecraft.block.Blocks;
import tech.krazyminer001.aquamarine.multiblocks.SimpleMember;

public class SimpleMembers {
    public static final SimpleMember STEEL_CASING = SimpleMember.ofBlock(() -> Blocks.IRON_BLOCK);
    public static final SimpleMember TERRACOTTA = SimpleMember.ofBlock(() -> Blocks.TERRACOTTA);
    public static final SimpleMember GRAPHITE = SimpleMember.ofBlock(() -> Blocks.COAL_BLOCK);
    public static final SimpleMember COKE = SimpleMember.ofBlock(() -> Blocks.COAL_BLOCK);
    public static final SimpleMember AIR = SimpleMember.ofBlock(() -> Blocks.AIR);
}
