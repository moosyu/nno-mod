package io.github.moosyu.recipes;

import java.util.List;

public class CraftingCountClientCache {
    private static List<Integer> requiredCounts = List.of();

    public static void set(List<Integer> counts) {
        requiredCounts = counts;
    }

    public static void setSlotCount(int slot, int count) {
        requiredCounts.set(slot, count);
    }

    public static int requiredFor(int slot) {
        return slot < requiredCounts.size() ? requiredCounts.get(slot) : 1;
    }
}
