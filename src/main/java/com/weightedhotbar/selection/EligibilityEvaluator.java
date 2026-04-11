package com.weightedhotbar.selection;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EligibilityEvaluator {

    public record Candidate(int slot, int stackSize) {}

    public static List<Candidate> evaluate(Inventory inventory, int lowerSlot, int upperSlot) {
        List<Candidate> candidates = new ArrayList<>();
        // Convert 1-indexed config slots to 0-indexed inventory slots
        int lower = lowerSlot - 1;
        int upper = upperSlot - 1;
        for (int i = lower; i <= upper; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                candidates.add(new Candidate(i, stack.getCount()));
            }
        }
        return candidates;
    }
}
