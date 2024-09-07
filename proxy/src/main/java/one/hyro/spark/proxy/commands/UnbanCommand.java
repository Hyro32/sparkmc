package one.hyro.spark.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class UnbanCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> unbanNode = BrigadierCommand.literalArgumentBuilder("unban")
                .requires(source -> source.hasPermission("spark.unban"))
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            String player = context.getArgument("player", String.class);

                            // Simulate unbanning the player and removing from database
                            // Database.removeBan(player);
                            source.sendMessage(Component.text(player + " has been unbanned.", NamedTextColor.GREEN));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(unbanNode);
    }
}
