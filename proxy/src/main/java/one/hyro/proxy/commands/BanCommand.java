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

import java.util.Map;

public class BanCommand {
    public static BrigadierCommand createBanCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("ban")
                .requires(source -> source.hasPermission("hyro.ban"))
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

                                    disconnectPLayerAndSaveBan(proxy, source, targetArgument, reasons.get(reasonArgument));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();

        return new BrigadierCommand(node);
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
