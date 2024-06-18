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

public class KickCommand {
    public static BrigadierCommand createKickCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("kick")
                .requires(source -> source.hasPermission("hyro.kick"))
                .executes(context -> {
                    CommandSource source = context.getSource();
                    source.sendMessage(Component.translatable("info.errors.providePLayer", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("argument", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            proxy.getAllPlayers().forEach(player -> builder.suggest(
                                    player.getUsername(),
                                    VelocityBrigadierMessage.tooltip(Component.text(player.getUsername()))
                            ));
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            String argumentProvided = context.getArgument("argument", String.class);

                            proxy.getPlayer(argumentProvided).ifPresentOrElse(player ->
                                            player.disconnect(Component.translatable("info.moderation.kick", NamedTextColor.RED)),
                                    () -> source.sendMessage(Component.translatable("info.errors.playerNotFound", NamedTextColor.RED))
                            );

                            Component successMessage = Component.translatable(
                                    "commands.kick.success",
                                    Component.text(argumentProvided, NamedTextColor.DARK_GREEN)
                            ).color(NamedTextColor.GREEN);

                            source.sendMessage(successMessage);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(node);
    }
}
