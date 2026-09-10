package dev.chickenstrips05.lukeui;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public static boolean shouldRound = false;
    public static boolean showAllBorders = true;
    public static boolean borders = true;
    public static boolean textShadow = false;
    public static String timeFormat = "hh:mm a";

    public static int wX = 10;
    public static int wY = 10;
    public static int width = 100;
    public static int height = 48;

    public static int leftMargin = 5;
    public static int topMargin = 5;
    public static float textScale = 0.5f;
    public static int textSpacing = 5;
    public static int borderThickness = 1;

    public static List<String> shownTexts = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(Lukeui.MOD_ID + ".json");

    private static class SaveData {
        boolean shouldRound = ModConfig.shouldRound;
        boolean showAllBorders = ModConfig.showAllBorders;
        boolean borders = ModConfig.borders;
        boolean textShadow = ModConfig.textShadow;
        String timeFormat = ModConfig.timeFormat;
        int wX = ModConfig.wX;
        int wY = ModConfig.wY;
        int width = ModConfig.width;
        int height = ModConfig.height;
        int leftMargin = ModConfig.leftMargin;
        int topMargin = ModConfig.topMargin;
        float textScale = ModConfig.textScale;
        int textSpacing = ModConfig.textSpacing;
        int borderThickness = ModConfig.borderThickness;
        List<String> shownTexts = new ArrayList<>(ModConfig.shownTexts);
    }

    private static void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(new SaveData(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                SaveData data = GSON.fromJson(reader, SaveData.class);
                if (data != null) {
                    shouldRound = data.shouldRound;
                    showAllBorders = data.showAllBorders;
                    borders = data.borders;
                    textShadow = data.textShadow;
                    timeFormat = data.timeFormat;
                    wX = data.wX;
                    wY = data.wY;
                    width = data.width;
                    height = data.height;
                    leftMargin = data.leftMargin;
                    topMargin = data.topMargin;
                    textScale = data.textScale;
                    textSpacing = data.textSpacing;
                    borderThickness = data.borderThickness;
                    shownTexts = data.shownTexts != null ? data.shownTexts : new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resetConfig();
        }
    }

    public static void resetConfig() {
        shouldRound = false;
        showAllBorders = true;
        borders = true;
        textShadow = false;
        timeFormat = "hh:mm a";
        wX = 10;
        wY = 10;
        width = 100;
        height = 48;
        leftMargin = 5;
        topMargin = 5;
        textScale = 0.5f;
        textSpacing = 5;
        borderThickness = 1;
        shownTexts = new ArrayList<>(Lukeui.elements);

        saveConfig();
    }

    public static void setBorders(boolean borders) { ModConfig.borders = borders; saveConfig(); }
    public static void setBorderThickness(int borderThickness) { ModConfig.borderThickness = borderThickness; saveConfig(); }
    public static void setHeight(int height) { ModConfig.height = height; saveConfig(); }
    public static void setLeftMargin(int leftMargin) { ModConfig.leftMargin = leftMargin; saveConfig(); }
    public static void setShouldRound(boolean shouldRound) { ModConfig.shouldRound = shouldRound; saveConfig(); }
    public static void setShowAllBorders(boolean showAllBorders) { ModConfig.showAllBorders = showAllBorders; saveConfig(); }
    public static void setTextShadow(boolean textShadow) { ModConfig.textShadow = textShadow; saveConfig(); }
    public static void setTimeFormat(String timeFormat) { ModConfig.timeFormat = timeFormat; saveConfig(); }
    public static void setTextScale(float textScale) { ModConfig.textScale = textScale; saveConfig(); }
    public static void setTextSpacing(int textSpacing) { ModConfig.textSpacing = textSpacing; saveConfig(); }
    public static void setTopMargin(int topMargin) { ModConfig.topMargin = topMargin; saveConfig(); }
    public static void setWidth(int width) { ModConfig.width = width; saveConfig(); }
    public static void setwX(int wX) { ModConfig.wX = wX; saveConfig(); }
    public static void setwY(int wY) { ModConfig.wY = wY; saveConfig(); }

    public static void showText(String element) {
        shownTexts.add(element);
        saveConfig();
    }

    public static void removeText(String element) {
        shownTexts.remove(element);
        saveConfig();
    }
}
