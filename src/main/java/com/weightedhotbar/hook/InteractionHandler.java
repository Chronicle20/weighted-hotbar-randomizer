package com.weightedhotbar.hook;

import com.weightedhotbar.config.ModConfig;
import com.weightedhotbar.feedback.FeedbackService;
import com.weightedhotbar.selection.EligibilityEvaluator;
import com.weightedhotbar.selection.WeightedSelector;
import net.minecraft.client.player.LocalPlayer;

import java.util.OptionalInt;

public class InteractionHandler {

    public static void onBlockPlaced(LocalPlayer player) {
        ModConfig config = ModConfig.getInstance();
        if (!config.isEnabled()) {
            return;
        }

        var inventory = player.getInventory();
        var candidates = EligibilityEvaluator.evaluate(inventory, config.getLowerSlot(), config.getUpperSlot());

        OptionalInt selected = WeightedSelector.select(candidates, player.getRandom());
        if (selected.isEmpty()) {
            return;
        }

        int newSlot = selected.getAsInt();
        if (newSlot == inventory.getSelectedSlot()) {
            return;
        }

        inventory.setSelectedSlot(newSlot);
        FeedbackService.showSlotSwitch(player, newSlot + 1);
    }
}
