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

import java.util.Optional;

public final class MessageCommand {

    private final ProxyServer proxy;

    public MessageCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    public BrigadierCommand createBrigadierCommand() {
        LiteralCommandNode<CommandSource> msgNode = BrigadierCommand.literalArgumentBuilder("msg")
                .requires(source -> source.hasPermission("spark.msg"))
                .then(BrigadierCommand.requiredArgumentBuilder("target", StringArgumentType.word())
                        .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    String targetName = context.getArgument("target", String.class);
                                    String message = context.getArgument("message", String.class);

                                    // Check if the sender is a player (only players can use /msg)
                                    if (!(source instanceof Player)) {
                                        source.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    Player sender = (Player) source;

                                    // Try to find the target player
                                    Optional<Player> targetOptional = proxy.getPlayer(targetName);
                                    if (targetOptional.isEmpty()) {
                                        sender.sendMessage(Component.text("Player " + targetName + " not found.", NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    Player target = targetOptional.get();

                                    // Send the message to both the sender and the recipient
                                    sendPrivateMessage(sender, target, message);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();

        return new BrigadierCommand(msgNode);
    }

    private void sendPrivateMessage(Player sender, Player recipient, String message) {
        // Create the message components
        Component senderMessage = Component.text("You -> " + recipient.getUsername() + ": " + message, NamedTextColor.GREEN);
        Component recipientMessage = Component.text(sender.getUsername() + " -> You: " + message, NamedTextColor.AQUA);

        // Send messages to both sender and recipient
        sender.sendMessage(senderMessage);
        recipient.sendMessage(recipientMessage);
    }
}
