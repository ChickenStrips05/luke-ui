package dev.chickenstrips05.lukeui;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static boolean shouldRound;
    public static boolean showAllBorders;
    public static boolean borders;

    public static int wX;
    public static int wY;
    public static int width;
    public static int height;

    public static int leftMargin;
    public static int topMargin;
    public static float textScale;
    public static int textSpacing;
    public static int borderThickness;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Path configPath = FabricLoader.getInstance().getConfigDir().resolve(Lukeui.MOD_ID + ".json");
    public static JsonObject config = new JsonObject();

    public static List<String> shownTexts = new ArrayList<>();
    public static final List<String> elements = Arrays.asList("fps", "ping", "tps", "position", "speed", "biome", "direction");

    private static void saveConfig() {
        try {
            Files.createDirectories(configPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {

                config = new JsonObject();

                config.addProperty("shouldRound", shouldRound);
                config.addProperty("showAllBorders", showAllBorders);
                config.addProperty("borders", borders);

                config.addProperty("wX", wX);
                config.addProperty("wY", wY);
                config.addProperty("width", width);
                config.addProperty("height", height);
                config.addProperty("leftMargin", leftMargin);
                config.addProperty("topMargin", topMargin);
                config.addProperty("textScale", textScale);
                config.addProperty("textSpacing", textSpacing);
                config.addProperty("borderThickness", borderThickness);
                config.add("shownTexts", GSON.toJsonTree(shownTexts));

                GSON.toJson(config, writer);
                System.out.println("Saved config");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (!Files.exists(configPath)) {
            System.out.println("Loading config for the first time");
            resetConfig();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            config = JsonParser.parseReader(reader).getAsJsonObject();

            shouldRound = config.get("shouldRound").getAsBoolean();
            showAllBorders = config.get("showAllBorders").getAsBoolean();
            borders = config.get("borders").getAsBoolean();

            wX = config.get("wX").getAsInt();
            wY = config.get("wY").getAsInt();
            width = config.get("width").getAsInt();
            height = config.get("height").getAsInt();
            leftMargin = config.get("leftMargin").getAsInt();
            topMargin = config.get("topMargin").getAsInt();
            textScale = config.get("textScale").getAsFloat();
            textSpacing = config.get("textSpacing").getAsInt();
            borderThickness = config.get("borderThickness").getAsInt();

            Type listType = new TypeToken<List<String>>() {}.getType();
            shownTexts = GSON.fromJson(config.get("shownTexts"), listType);

            if (shownTexts == null) {
                shownTexts = new ArrayList<>(elements);
            }

            System.out.println("Loaded config");
        } catch (IOException | JsonParseException | NullPointerException e ) {
            e.printStackTrace();
            System.out.println("Parsing or loading config failed, resetting");
            resetConfig();
        }
    }

    public static void resetConfig() {
        shouldRound = false;
        showAllBorders = true;
        borders = true;

        wX = 10;
        wY = 10;
        width = 130;
        height = 45;
        leftMargin = 5;
        topMargin = 5;
        textScale = 0.5f;
        textSpacing = 5;
        borderThickness = 1;
        shownTexts = new ArrayList<>(elements);

        saveConfig();
    }

    public static void setBorders(boolean borders) {
        Config.borders = borders;
        saveConfig();
    }

    public static void setBorderThickness(int borderThickness) {
        Config.borderThickness = borderThickness;
        saveConfig();
    }

    public static void setHeight(int height) {
        Config.height = height;
        saveConfig();
    }

    public static void setLeftMargin(int leftMargin) {
        Config.leftMargin = leftMargin;
        saveConfig();
    }

    public static void setShouldRound(boolean shouldRound) {
        Config.shouldRound = shouldRound;
        saveConfig();
    }

    public static void setShowAllBorders(boolean showAllBorders) {
        Config.showAllBorders = showAllBorders;
        saveConfig();
    }

    public static void setTextScale(float textScale) {
        Config.textScale = textScale;
        saveConfig();
    }

    public static void setTextSpacing(int textSpacing) {
        Config.textSpacing = textSpacing;
        saveConfig();
    }

    public static void setTopMargin(int topMargin) {
        Config.topMargin = topMargin;
        saveConfig();
    }

    public static void setWidth(int width) {
        Config.width = width;
        saveConfig();
    }

    public static void setwX(int wX) {
        Config.wX = wX;
        saveConfig();
    }

    public static void setwY(int wY) {
        Config.wY = wY;
        saveConfig();
    }

    public static void showText(String element) {
        shownTexts.add(element);
        saveConfig();
    }

    public static void removeText(String element) {
        shownTexts.remove(element);
        saveConfig();
    }
}
