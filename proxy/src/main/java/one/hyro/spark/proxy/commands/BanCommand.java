package one.hyro.spark.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class BanCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> banNode = BrigadierCommand.literalArgumentBuilder("ban")
                .requires(source -> source.hasPermission("spark.ban"))
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.greedyString())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    String player = context.getArgument("player", String.class);
                                    String reason = context.getArgument("reason", String.class);

                                    // Simulate banning the player and saving to database
                                    // Database.saveBan(player, reason, permanent=true);
                                    source.sendMessage(Component.text(player + " has been permanently banned for: " + reason, NamedTextColor.RED));

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();

        return new BrigadierCommand(banNode);
    }
}
