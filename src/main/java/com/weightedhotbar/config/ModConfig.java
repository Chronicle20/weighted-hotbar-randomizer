package com.weightedhotbar.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weightedhotbar.WeightedHotbarRandomizer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("weightedhotbar.json");

    private static ModConfig instance;

    private boolean enabled = false;
    private int lowerSlot = 1;
    private int upperSlot = 9;

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        save();
    }

    public void toggleEnabled() {
        setEnabled(!enabled);
    }

    public int getLowerSlot() {
        return lowerSlot;
    }

    public int getUpperSlot() {
        return upperSlot;
    }

    public void setRange(int lower, int upper) {
        this.lowerSlot = clampSlot(lower);
        this.upperSlot = clampSlot(upper);
        if (this.lowerSlot > this.upperSlot) {
            int temp = this.lowerSlot;
            this.lowerSlot = this.upperSlot;
            this.upperSlot = temp;
        }
        save();
    }

    private static int clampSlot(int slot) {
        return Math.max(1, Math.min(9, slot));
    }

    public static ModConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                ModConfig config = GSON.fromJson(json, ModConfig.class);
                if (config != null) {
                    config.validate();
                    instance = config;
                    return config;
                }
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                WeightedHotbarRandomizer.LOGGER.warn("Failed to load config, using defaults", e);
            }
        }
        ModConfig config = new ModConfig();
        config.save();
        instance = config;
        return config;
    }

    private void validate() {
        lowerSlot = clampSlot(lowerSlot);
        upperSlot = clampSlot(upperSlot);
        if (lowerSlot > upperSlot) {
            int temp = lowerSlot;
            lowerSlot = upperSlot;
            upperSlot = temp;
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            WeightedHotbarRandomizer.LOGGER.error("Failed to save config", e);
        }
    }
}
