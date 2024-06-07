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

public class PingCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (stack.getExecutor() instanceof Player player) {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) return;

                TextComponent message = Component.text("The player ", NamedTextColor.GREEN)
                        .append(Component.text(args[0], NamedTextColor.DARK_GREEN))
                        .append(Component.text(" has a ping of ", NamedTextColor.GREEN))
                        .append(Component.text(target.getPing() + " ms.", NamedTextColor.DARK_GREEN));

                player.sendMessage(message);
                return;
            }

            TextComponent message = Component.text("Your ping is: ", NamedTextColor.GREEN)
                    .append(Component.text(player.getPing() + " ms.", NamedTextColor.DARK_GREEN));

            player.sendMessage(message);
        } else {
            Bukkit.getLogger().warning("&4This command can only be executed by a player!");
        }
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
