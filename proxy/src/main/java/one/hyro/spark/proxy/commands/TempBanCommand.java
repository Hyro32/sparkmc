package one.hyro.spark.proxy.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class TempBanCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> tempBanNode = BrigadierCommand.literalArgumentBuilder("tempban")
                .requires(source -> source.hasPermission("spark.tempban"))
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.greedyString())
                                .then(BrigadierCommand.requiredArgumentBuilder("time", LongArgumentType.longArg())
                                        .executes(context -> {
                                            CommandSource source = context.getSource();
                                            String player = context.getArgument("player", String.class);
                                            String reason = context.getArgument("reason", String.class);
                                            long time = context.getArgument("time", Long.class);

                                            // Simulate temp-banning the player and saving to database
                                            // Database.saveTempBan(player, reason, time);
                                            source.sendMessage(Component.text(player + " has been temporarily banned for " + time + " minutes for: " + reason, NamedTextColor.RED));

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .build();

        return new BrigadierCommand(tempBanNode);
    }
}
