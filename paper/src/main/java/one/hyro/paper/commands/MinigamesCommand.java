package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import one.hyro.paper.managers.MenusManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinigamesCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length > 0) return;
        if (!(stack.getExecutor() instanceof Player player)) return;
        MenusManager.openMenu(player, "minigames");
    }
}
