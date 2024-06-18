package one.hyro.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Date;
import java.util.Map;

public class TempBanCommand {
    public static BrigadierCommand createTempBanCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("tempban")
                .requires(source -> source.hasPermission("hyro.tempban"))
                .executes(context -> {
                    CommandSource source = context.getSource();
                    source.sendMessage(Component.translatable("info.errors.providePLayer", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("target", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            proxy.getAllPlayers().forEach(player -> builder.suggest(
                                    player.getUsername(),
                                    VelocityBrigadierMessage.tooltip(Component.text(player.getUsername()))
                            ));
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            source.sendMessage(Component.translatable("info.errors.provideTime", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("time", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("1s", VelocityBrigadierMessage.tooltip(Component.text("1s")));
                                    builder.suggest("1m", VelocityBrigadierMessage.tooltip(Component.text("1m")));
                                    builder.suggest("1h", VelocityBrigadierMessage.tooltip(Component.text("1h")));
                                    builder.suggest("1d", VelocityBrigadierMessage.tooltip(Component.text("1d")));
                                    builder.suggest("1w", VelocityBrigadierMessage.tooltip(Component.text("1w")));
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    source.sendMessage(Component.translatable("info.errors.provideReason", NamedTextColor.RED));
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            Map<String, String> reasons = Map.of(
                                                    "Hacks", "info.reasons.hacks",
                                                    "Spamming", "info.reasons.spam",
                                                    "Offensive", "info.reasons.offensive",
                                                    "Other", "info.reasons.other",
                                                    "None", "info.reasons.none"
                                            );

                                            for (Map.Entry<String, String> entry : reasons.entrySet()) {
                                                builder.suggest(
                                                        entry.getKey(),
                                                        VelocityBrigadierMessage.tooltip(Component.text(entry.getKey()))
                                                );
                                            }

                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            CommandSource source = context.getSource();
                                            String reasonArgument = context.getArgument("reason", String.class);
                                            String targetArgument = context.getArgument("target", String.class);
                                            String timeArgument = context.getArgument("time", String.class);
                                            Date finalDate = parseBanTime(timeArgument);

                                            Map<String, String> reasons = Map.of(
                                                    "Hacks", "info.reasons.hacks",
                                                    "Spamming", "info.reasons.spam",
                                                    "Offensive", "info.reasons.offensive",
                                                    "Other", "info.reasons.other",
                                                    "None", "info.reasons.none"
                                            );

                                            if (!reasons.containsKey(reasonArgument)) {
                                                source.sendMessage(Component.translatable("info.errors.invalidReason", NamedTextColor.RED));
                                                return Command.SINGLE_SUCCESS;
                                            }

                                            if (finalDate == null) {
                                                source.sendMessage(Component.translatable("info.errors.invalidTime", NamedTextColor.RED));
                                                return Command.SINGLE_SUCCESS;
                                            }

                                            disconnectPLayerAndSaveBan(proxy, source, targetArgument, timeArgument, reasons.get(reasonArgument));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .build();
        return new BrigadierCommand(node);
    }

    private static Date parseBanTime(String time) {
        String timeUnit = time.substring(time.length() - 1);
        String timeValue = time.substring(0, time.length() - 1);
        Date finalDate;

        switch (timeUnit) {
            case "s" -> finalDate = new Date(System.currentTimeMillis() + Integer.parseInt(timeValue) * 1000L);
            case "m" -> finalDate = new Date(System.currentTimeMillis() + Integer.parseInt(timeValue) * 60 * 1000L);
            case "h" -> finalDate = new Date(System.currentTimeMillis() + Integer.parseInt(timeValue) * 3600 * 1000L);
            case "d" -> finalDate = new Date(System.currentTimeMillis() + Integer.parseInt(timeValue) * 24 * 3600 * 1000L);
            case "w" -> finalDate = new Date(System.currentTimeMillis() + Integer.parseInt(timeValue) * 7 * 24 * 3600 * 1000L);
            default -> finalDate = null;
        }
        return finalDate;
    }

    private static void disconnectPLayerAndSaveBan(final ProxyServer proxy, CommandSource source, String... arguments) {
        proxy.getPlayer(arguments[0])
                .ifPresentOrElse(player -> {
                            player.disconnect(Component.translatable("info.moderation.ban", NamedTextColor.RED));

                            // Save ban to database

                            Component successMessage = Component.translatable(
                                    "commands.ban.success",
                                    Component.text(arguments[0], NamedTextColor.DARK_GREEN)
                            ).color(NamedTextColor.GREEN);
                            source.sendMessage(successMessage);
                        },
                        () -> source.sendMessage(Component.translatable("info.errors.playerNotFound", NamedTextColor.RED))
                );
    }
}
