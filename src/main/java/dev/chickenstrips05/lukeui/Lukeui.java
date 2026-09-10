package dev.chickenstrips05.lukeui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.joml.Matrix3x2fStack;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Lukeui implements ClientModInitializer {
    static public String MOD_ID = "lukeui";
    public static final List<String> elements = Arrays.asList("fps", "ping", "tps", "position", "speed", "biome", "direction", "time");

    private int textY;

    private int fps;
    private int ping;
    private float tps;

    private boolean localWorld;
    private double x;
    private double y;
    private double z;

    private String playerDir;
    private float yaw;
    private float pitch;

    private double speed;
    private String biome;

    private String timeString;

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, Identifier.of(MOD_ID, "after_chat"), layer());

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            ClientWorld world = client.world;

            if (player != null && world != null) {
                fps = client.getCurrentFps();
                tps = TickRate.getTps();
                x = player.getX();
                y = player.getY();
                z = player.getZ();

                BlockPos pos = player.getBlockPos();
                RegistryEntry<Biome> biomeRegistryEntry = world.getBiome(pos);

                Identifier biomeIdentifier = biomeRegistryEntry.getKey().map(RegistryKey::getValue).orElse(null);
                if (biomeIdentifier != null) {
                    biome = biomeIdentifier.getPath();
                } else {
                    biome = "Unknown";
                }

                localWorld = client.isConnectedToLocalServer();

                if (client.getNetworkHandler() != null && !localWorld) {
                    PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
                    if (entry != null) {
                        ping = entry.getLatency();
                    } else {
                        ping = 0;
                    }
                }

                double deltaX = player.getX() - player.lastX;
                double deltaZ = player.getZ() - player.lastZ;

                speed = Math.hypot(deltaX, deltaZ) * 20.0;

                playerDir = player.getHorizontalFacing().asString();
                yaw = MathHelper.wrapDegrees(player.getYaw());
                pitch = player.getPitch();

                timeString = LocalTime.now().format(DateTimeFormatter.ofPattern(ModConfig.timeFormat));
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> TickRate.reset());

        ModConfig.loadConfig();
        ModCommands.init();
    }

    private void nextY() {
        textY = (int) (textY + (ModConfig.textSpacing / ModConfig.textScale));
    }

    private HudElement layer() {
        return (graphics, deltaTracker) -> {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            Matrix3x2fStack matrices = graphics.getMatrices();
            int textColor = -2302751;
            int outlineColor = -1778333441;
            boolean shadow = ModConfig.textShadow;



            graphics.fill(ModConfig.wX, ModConfig.wY, ModConfig.wX + ModConfig.width, ModConfig.wY + ModConfig.height, 838860800);

            if (ModConfig.borders) {
                graphics.fill(ModConfig.wX - ModConfig.borderThickness, ModConfig.wY - ModConfig.borderThickness, ModConfig.wX, ModConfig.wY + ModConfig.height + ModConfig.borderThickness, outlineColor);

                if (ModConfig.showAllBorders) {
                    graphics.fill(ModConfig.wX, ModConfig.wY + ModConfig.height, ModConfig.wX + ModConfig.width + ModConfig.borderThickness, ModConfig.wY + ModConfig.height + ModConfig.borderThickness, outlineColor);
                    graphics.fill(ModConfig.wX + ModConfig.width, ModConfig.wY + ModConfig.height, ModConfig.wX + ModConfig.width + ModConfig. borderThickness, ModConfig.wY - ModConfig.borderThickness, outlineColor);
                    graphics.fill(ModConfig.wX + ModConfig.width, ModConfig.wY, ModConfig.wX, ModConfig.wY - ModConfig.borderThickness, outlineColor);
                }
            }

            matrices.pushMatrix();
            matrices.scale(ModConfig.textScale, ModConfig.textScale);
            String posText;

            if (ModConfig.shouldRound) {
                posText = String.format("Pos: %d, %d, %d", (int) x, (int) y, (int) z);
            } else {
                posText = String.format("Pos: %.1f, %.1f, %.1f", x, y, z);
            }

            String pingText;
            if (localWorld) {
                pingText = "Ping: 0 ms (local)";
            } else {
                if (ping == 0) {
                    pingText = "Ping: ...";
                } else {
                    pingText = "Ping: " + ping + " ms";
                }
            }


            int textX = (int) ((ModConfig.wX + ModConfig.leftMargin) / ModConfig.textScale);
            textY = (int) ((ModConfig.wY + ModConfig.topMargin) / ModConfig.textScale);


            if (ModConfig.shownTexts.contains("fps")) {
                graphics.drawText(renderer, "FPS: " + fps, textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("ping")) {
                graphics.drawText(renderer, pingText, textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("tps")) {
                graphics.drawText(renderer, String.format("TPS: %.1f", tps), textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("position")) {
                graphics.drawText(renderer, posText, textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("speed")) {
                graphics.drawText(renderer, String.format("Speed: %.1f b/s", speed), textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("biome")) {
                graphics.drawText(renderer, "Biome: " + TitleCase.titleCase(biome), textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("direction")) {
                graphics.drawText(renderer, String.format("%s (%.1f, %.1f)", TitleCase.titleCase(playerDir), yaw, pitch), textX, textY, textColor, shadow);
                nextY();
            }

            if (ModConfig.shownTexts.contains("time")) {
                graphics.drawText(renderer, "Time: " + timeString, textX, textY, textColor, shadow);
                nextY();
            }

            matrices.popMatrix();
        };
    }
}
