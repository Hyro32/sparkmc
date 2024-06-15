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

public class KickCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (stack.getExecutor() instanceof Player player && !player.hasPermission("hyros.kick")) {
            player.sendMessage(Component.translatable("info.errors.permissions").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 1) return;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            stack.getSender().sendMessage(Component.translatable("info.errors.playerNotFound").color(NamedTextColor.RED));
            return;
        }

        target.kick(Component.translatable("info.moderation.kick").color(NamedTextColor.RED));
        Component successMessage = Component.translatable(
                "info.success.kick",
                Component.text(target.getName()).color(NamedTextColor.DARK_GREEN)
        ).color(NamedTextColor.GREEN);
        stack.getSender().sendMessage(successMessage);
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
