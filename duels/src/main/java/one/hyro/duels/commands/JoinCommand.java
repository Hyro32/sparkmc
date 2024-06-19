package one.hyro.duels.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import one.hyro.duels.managers.QueueManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class JoinCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;

        if (args.length == 1) {
            QueueManager queueManager = new QueueManager();
            switch (args[0].toLowerCase()) {
                case "singles" -> queueManager.addToSinglesQueue(player);
                case "doubles" -> queueManager.addToDoublesQueue(player);
                default -> player.sendMessage("Invalid queue type!");
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 1) return List.of("singles", "doubles");
        return List.of();
    }
}
