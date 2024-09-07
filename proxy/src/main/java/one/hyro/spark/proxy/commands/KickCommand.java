package one.hyro.spark.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class KickCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> kickNode = BrigadierCommand.literalArgumentBuilder("kick")
                .requires(source -> source.hasPermission("spark.kick"))
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.greedyString())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    String player = context.getArgument("player", String.class);
                                    String reason = context.getArgument("reason", String.class);

                                    // Simulate kicking the player
                                    // Database.saveKick(player, reason);
                                    source.sendMessage(Component.text(player + " has been kicked for: " + reason, NamedTextColor.YELLOW));

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();

        return new BrigadierCommand(kickNode);
    }
}
