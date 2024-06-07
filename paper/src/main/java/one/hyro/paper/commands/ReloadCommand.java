package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (stack.getExecutor() instanceof Player player && !player.hasPermission("hyro.reload")) {
            TextComponent message = Component.text("You don't have permission to execute this command!", NamedTextColor.RED);
            player.sendMessage(message);
            return;
        }

        try {
            HyrosPaper.getInstance().reloadConfig();
            TextComponent message = Component.text("Config reloaded successfully!", NamedTextColor.GREEN);
            stack.getSender().sendMessage(message);
        } catch (Exception e) {
            TextComponent message = Component.text("Error while reloading config!", NamedTextColor.RED);
            stack.getSender().sendMessage(message);
            e.printStackTrace();
        }
    }
}
