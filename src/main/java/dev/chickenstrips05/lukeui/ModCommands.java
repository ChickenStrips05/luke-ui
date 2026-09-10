package dev.chickenstrips05.lukeui;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class ModCommands {
    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("lukeui")
                            .then(literal("config")
                                    .then(literal("border")
                                            .then(argument("state", BoolArgumentType.bool())
                                                    .executes(context -> {
                                                        ModConfig.setBorders(BoolArgumentType.getBool(context, "state"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("allBorders")
                                            .then(argument("state", BoolArgumentType.bool())
                                                    .executes(context -> {
                                                        ModConfig.setShowAllBorders(BoolArgumentType.getBool(context, "state"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("roundPosition")
                                            .then(argument("state", BoolArgumentType.bool())
                                                    .executes(context -> {
                                                        ModConfig.setShouldRound(BoolArgumentType.getBool(context, "state"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("windowX")
                                            .then(argument("x", IntegerArgumentType.integer(0, 200))
                                                    .executes(context -> {
                                                        ModConfig.setwX(IntegerArgumentType.getInteger(context, "x"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("windowY")
                                            .then(argument("y", IntegerArgumentType.integer(0, 200))
                                                    .executes(context -> {
                                                        ModConfig.setwY(IntegerArgumentType.getInteger(context, "y"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("windowWidth")
                                            .then(argument("width", IntegerArgumentType.integer(0, 200))
                                                    .executes(context -> {
                                                        ModConfig.setWidth(IntegerArgumentType.getInteger(context, "width"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("windowHeight")
                                            .then(argument("height", IntegerArgumentType.integer(0, 200))
                                                    .executes(context -> {
                                                        ModConfig.setHeight(IntegerArgumentType.getInteger(context, "height"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("borderThickness")
                                            .then(argument("thickness", IntegerArgumentType.integer(1, 10))
                                                    .executes(context -> {
                                                        ModConfig.setBorderThickness(IntegerArgumentType.getInteger(context, "thickness"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("leftMargin")
                                            .then(argument("margin", IntegerArgumentType.integer(0, 50))
                                                    .executes(context -> {
                                                        ModConfig.setLeftMargin(IntegerArgumentType.getInteger(context, "margin"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("topMargin")
                                            .then(argument("margin", IntegerArgumentType.integer(0, 50))
                                                    .executes(context -> {
                                                        ModConfig.setTopMargin(IntegerArgumentType.getInteger(context, "margin"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("textScale")
                                            .then(argument("scale", FloatArgumentType.floatArg(0.1f, 3.0f))
                                                    .executes(context -> {
                                                        ModConfig.setTextScale(FloatArgumentType.getFloat(context, "scale"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("textSpacing")
                                            .then(argument("spacing", IntegerArgumentType.integer(1, 50))
                                                    .executes(context -> {
                                                        ModConfig.setTextSpacing(IntegerArgumentType.getInteger(context, "spacing"));
                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(literal("timeFormat")
                                            .then(argument("format", StringArgumentType.greedyString())
                                                    .executes(context -> {
                                                        ModConfig.setTimeFormat(StringArgumentType.getString(context, "format"));
                                                        return 1;
                                                    })
                                            ))

                                    .then(literal("textShadow")
                                            .then(argument("shadow", BoolArgumentType.bool())
                                                   .executes(context -> {
                                                        ModConfig.setTextShadow(BoolArgumentType.getBool(context, "shadow"));
                                                        return 1;
                                                })
                                            ))

                                    .then(literal("reset")
                                            .executes(context -> {
                                                ModConfig.resetConfig();
                                                return 1;
                                            })
                                    )
                            )

                            .then(literal("show")
                                    .then(argument("element", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                for (String suggestion : Lukeui.elements) {
                                                    if (suggestion.toLowerCase().startsWith(builder.getRemainingLowerCase()) && !ModConfig.shownTexts.contains(suggestion)) {
                                                        builder.suggest(suggestion);
                                                    }
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(context -> {
                                                String element = StringArgumentType.getString(context, "element");

                                                if (Lukeui.elements.contains(element)) {
                                                    if (!ModConfig.shownTexts.contains(element)) {
                                                        ModConfig.showText(element);
                                                    } else {
                                                        context.getSource().sendFeedback(Text.literal("Element is already being displayed").withColor(Colors.LIGHT_RED));
                                                    }
                                                } else {
                                                    context.getSource().sendFeedback(Text.literal("Unknown element").withColor(Colors.LIGHT_RED));
                                                }
                                                return 1;
                                            })
                            ))

                            .then(literal("hide")
                                    .then(argument("element", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                for (String suggestion : ModConfig.shownTexts) {
                                                    if (suggestion.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                                                        builder.suggest(suggestion);
                                                    }
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(context -> {
                                                String element = StringArgumentType.getString(context, "element");

                                                if (Lukeui.elements.contains(element)) {
                                                    if (ModConfig.shownTexts.contains(element)) {
                                                        ModConfig.removeText(element);
                                                    } else {
                                                        context.getSource().sendFeedback(Text.literal("Element is already hidden").withColor(Colors.LIGHT_RED));
                                                    }
                                                } else {
                                                    context.getSource().sendFeedback(Text.literal("Unknown element").withColor(Colors.LIGHT_RED));
                                                }
                                                return 1;
                                            })
                                    )
                            )
            );
        });
    }
}
