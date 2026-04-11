package com.weightedhotbar;

import com.weightedhotbar.command.HotbarRandCommand;
import com.weightedhotbar.config.ModConfig;
import com.weightedhotbar.feedback.FeedbackService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeightedHotbarRandomizer implements ClientModInitializer {

    public static final String MOD_ID = "weightedhotbar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyMapping toggleKeybind;

    @Override
    public void onInitializeClient() {
        ModConfig.getInstance();

        toggleKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.weightedhotbar.toggle",
                InputConstants.UNKNOWN.getValue(),
                KeyMapping.Category.MISC
        ));

        HotbarRandCommand.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeybind.consumeClick()) {
                ModConfig config = ModConfig.getInstance();
                config.toggleEnabled();
                if (client.player != null) {
                    FeedbackService.showToggle(client.player, config.isEnabled());
                }
            }
        });

        LOGGER.info("Weighted Hotbar Randomizer loaded");
    }
}
