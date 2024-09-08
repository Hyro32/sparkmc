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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class TempBanCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> tempBanNode = BrigadierCommand.literalArgumentBuilder("tempban")
                .requires(source -> source.hasPermission("spark.tempban"))
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
                            source.sendMessage(Component.translatable("context.error.specifyTime", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("time", StringArgumentType.word())
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
                                            String time = context.getArgument("time", String.class);
                                            Date expiry = parseTime(time);
                                            Player target = proxy.getPlayer(player).orElse(null);

                                            if (target == null) {
                                                source.sendMessage(Component.translatable("context.error.playerOffline", NamedTextColor.RED));
                                                return Command.SINGLE_SUCCESS;
                                            }

                                            if (expiry == null) {
                                                source.sendMessage(Component.translatable("context.error.invalidTimeFormat", NamedTextColor.RED));
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
                )
                .build();

        return new BrigadierCommand(tempBanNode);
    }

    private static Date parseTime(String time) {
        String regex = "(\\d+)([dhms])";
        boolean matches = time.matches(regex);
        String unit = time.substring(time.length() - 1);
        int amount = Integer.parseInt(time.substring(0, time.length() - 1));

        if (!matches) return null;
        Calendar calendar = Calendar.getInstance();
        switch (unit) {
            case "d" -> calendar.add(Calendar.DAY_OF_MONTH, amount);
            case "h" -> calendar.add(Calendar.HOUR, amount);
            case "m" -> calendar.add(Calendar.MINUTE, amount);
            case "s" -> calendar.add(Calendar.SECOND, amount);
            default -> {
                return null;
            }
        }

        return calendar.getTime();
    }
}
