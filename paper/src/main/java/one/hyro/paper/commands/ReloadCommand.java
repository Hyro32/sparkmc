package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.managers.MenusManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (stack.getExecutor() instanceof Player player && !player.hasPermission("hyro.reload")) {
            player.sendMessage(Component.translatable("info.errors.permissions").color(NamedTextColor.RED));
            return;
        }

        try {
            MenusManager.loadMenus();
            HyrosPaper.getInstance().reloadConfig();
            stack.getSender().sendMessage(Component.translatable("info.success.reload").color(NamedTextColor.GREEN));
        } catch (Exception e) {
            stack.getSender().sendMessage(Component.translatable("info.errors.reload").color(NamedTextColor.RED));
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}
