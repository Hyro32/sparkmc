package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PingCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(Component.translatable("info.errors.playerNotFound").color(NamedTextColor.RED));
                return;
            }

            Component playerPing = Component.translatable(
                    "commands.ping.player",
                    Component.text(target.getName(), NamedTextColor.DARK_GREEN),
                    Component.text(target.getPing(), NamedTextColor.DARK_GREEN)
            ).color(NamedTextColor.GREEN);

            player.sendMessage(playerPing);
            return;
        }

        Component message = Component.translatable(
                "commands.ping.self",
                Component.text(player.getPing(), NamedTextColor.DARK_GREEN)
        ).color(NamedTextColor.GREEN);

        player.sendMessage(message);
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
