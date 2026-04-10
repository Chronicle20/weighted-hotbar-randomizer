package com.weightedhotbar.feedback;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class FeedbackService {

    public static void showSlotSwitch(LocalPlayer player, int slot) {
        player.sendOverlayMessage(
                Component.literal("Slot " + slot).withStyle(ChatFormatting.GRAY)
        );
    }

    public static void showToggle(LocalPlayer player, boolean enabled) {
        player.sendOverlayMessage(
                Component.literal("Weighted Hotbar: ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(enabled ? "ON" : "OFF")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED))
        );
    }

    public static void showStatus(FabricClientCommandSource source, boolean enabled, int lower, int upper) {
        source.sendFeedback(
                Component.literal("Weighted Hotbar Randomizer").withStyle(ChatFormatting.GOLD)
        );
        source.sendFeedback(
                Component.literal("  Status: ")
                        .append(Component.literal(enabled ? "Enabled" : "Disabled")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED))
        );
        source.sendFeedback(
                Component.literal("  Slot range: ")
                        .append(Component.literal(lower + " - " + upper).withStyle(ChatFormatting.AQUA))
        );
    }

    public static void showRangeSet(FabricClientCommandSource source, int lower, int upper) {
        source.sendFeedback(
                Component.literal("Slot range set to ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(lower + " - " + upper).withStyle(ChatFormatting.AQUA))
        );
    }
}
