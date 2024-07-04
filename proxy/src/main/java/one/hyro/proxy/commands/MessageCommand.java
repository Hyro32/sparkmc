package one.hyro.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageCommand {
    public static BrigadierCommand createMessageCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("message")
                //.requires(source -> source.hasPermission("hyro.message"))
                .executes(context -> {
                    CommandSource source = context.getSource();
                    source.sendMessage(Component.translatable("info.errors.providePLayer", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            proxy.getAllPlayers().forEach(player -> builder.suggest(
                                    player.getUsername(),
                                    VelocityBrigadierMessage.tooltip(Component.text(player.getUsername()))
                            ));
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            source.sendMessage(Component.translatable("info.errors.providePLayer", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String message = StringArgumentType.getString(context, "message");
                                    String receiverName = StringArgumentType.getString(context, "player");
                                    Player receiver = proxy.getPlayer(receiverName).orElse(null);
                                    Player source = (Player) context.getSource();

                                    if (receiver == null) {
                                        context.getSource().sendMessage(Component.translatable("info.errors.playerNotFound", NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    Component messageComponent = Component.translatable(
                                            "commands.message.private",
                                            Component.text(source.getUsername()),
                                            Component.text(message, NamedTextColor.GRAY)
                                    ).color(NamedTextColor.DARK_GRAY);

                                    source.sendMessage(messageComponent);
                                    receiver.sendMessage(messageComponent.clickEvent(ClickEvent.suggestCommand("/message " + source.getUsername())));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
        return new BrigadierCommand(node);
    }
}
