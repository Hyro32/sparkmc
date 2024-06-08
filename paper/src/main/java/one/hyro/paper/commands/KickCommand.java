package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KickCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            TextComponent message = Component.text("Player not found", NamedTextColor.RED);
            stack.getSender().sendMessage(message);
            return;
        }

        if (stack.getExecutor() instanceof Player player && !player.hasPermission("hyros.kick")) return;

        target.kick(Component.text("You have been kicked from the server.", NamedTextColor.RED));
        TextComponent message = Component.text("Player has been kicked from the server.", NamedTextColor.GREEN);
        stack.getSender().sendMessage(message);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers())
                playerNames.add(player.getName());
            return playerNames;
        }
        return List.of();
    }
}
