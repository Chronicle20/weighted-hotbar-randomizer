package com.weightedhotbar.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.weightedhotbar.config.ModConfig;
import com.weightedhotbar.feedback.FeedbackService;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class HotbarRandCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal("weightedhotbar")
                        .then(literal("toggle").executes(HotbarRandCommand::toggle))
                        .then(literal("on").executes(HotbarRandCommand::enable))
                        .then(literal("off").executes(HotbarRandCommand::disable))
                        .then(literal("status").executes(HotbarRandCommand::status))
                        .then(literal("range")
                                .then(argument("lower", IntegerArgumentType.integer(1, 9))
                                        .then(argument("upper", IntegerArgumentType.integer(1, 9))
                                                .executes(HotbarRandCommand::setRange))))
                )
        );
    }

    private static int toggle(CommandContext<FabricClientCommandSource> ctx) {
        ModConfig config = ModConfig.getInstance();
        config.toggleEnabled();
        FeedbackService.showToggle(ctx.getSource().getPlayer(), config.isEnabled());
        return 1;
    }

    private static int enable(CommandContext<FabricClientCommandSource> ctx) {
        ModConfig config = ModConfig.getInstance();
        config.setEnabled(true);
        FeedbackService.showToggle(ctx.getSource().getPlayer(), true);
        return 1;
    }

    private static int disable(CommandContext<FabricClientCommandSource> ctx) {
        ModConfig config = ModConfig.getInstance();
        config.setEnabled(false);
        FeedbackService.showToggle(ctx.getSource().getPlayer(), false);
        return 1;
    }

    private static int status(CommandContext<FabricClientCommandSource> ctx) {
        ModConfig config = ModConfig.getInstance();
        FeedbackService.showStatus(ctx.getSource(), config.isEnabled(), config.getLowerSlot(), config.getUpperSlot());
        return 1;
    }

    private static int setRange(CommandContext<FabricClientCommandSource> ctx) {
        int lower = IntegerArgumentType.getInteger(ctx, "lower");
        int upper = IntegerArgumentType.getInteger(ctx, "upper");

        if (lower > upper) {
            ctx.getSource().sendError(
                    net.minecraft.network.chat.Component.literal("Lower slot must be <= upper slot")
            );
            return 0;
        }

        ModConfig config = ModConfig.getInstance();
        config.setRange(lower, upper);
        FeedbackService.showRangeSet(ctx.getSource(), lower, upper);
        return 1;
    }
}
