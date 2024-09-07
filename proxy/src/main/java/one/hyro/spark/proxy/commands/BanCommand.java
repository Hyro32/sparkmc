package one.hyro.spark.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public final class BanCommand {
    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> banNode = BrigadierCommand.literalArgumentBuilder("ban")
                .requires(source -> source.hasPermission("spark.ban"))
                .executes(context -> {
                    CommandSource source = context.getSource();
                    source.sendMessage(Component.translatable("context.error.specifyPlayer", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            List<String> onlinePlayers = proxy.getAllPlayers().stream().map(Player::getUsername).toList();
                            onlinePlayers.forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            source.sendMessage(Component.translatable("context.error.specifyReason", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    // Suggest reasons from a list
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    String player = context.getArgument("player", String.class);
                                    String reason = context.getArgument("reason", String.class);
                                    Player target = proxy.getPlayer(player).orElse(null);

                                    if (target == null) {
                                        source.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    Component successMessage = Component.translatable(
                                            "context.success.ban",
                                            Component.text(target.getUsername(), NamedTextColor.DARK_GREEN)
                                    ).color(NamedTextColor.GREEN);

                                    // Call API to save ban to database
                                    target.disconnect(Component.translatable("context.success.banMessage", NamedTextColor.RED));
                                    source.sendMessage(successMessage);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();

        return new BrigadierCommand(banNode);
    }
}
