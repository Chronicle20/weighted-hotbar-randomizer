package com.weightedhotbar.selection;

import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.OptionalInt;

public class WeightedSelector {

    public static OptionalInt select(List<EligibilityEvaluator.Candidate> candidates, RandomSource random) {
        if (candidates.isEmpty()) {
            return OptionalInt.empty();
        }
        if (candidates.size() == 1) {
            return OptionalInt.of(candidates.getFirst().slot());
        }

        int totalWeight = 0;
        for (var candidate : candidates) {
            totalWeight += candidate.stackSize();
        }

        int roll = random.nextInt(totalWeight);
        int cumulative = 0;
        for (var candidate : candidates) {
            cumulative += candidate.stackSize();
            if (roll < cumulative) {
                return OptionalInt.of(candidate.slot());
            }
        }

        // Should not reach here, but return last candidate as fallback
        return OptionalInt.of(candidates.getLast().slot());
    }
}
