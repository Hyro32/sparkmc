package one.hyro.duels.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import one.hyro.duels.HyroDuels;
import one.hyro.duels.managers.QueueManager;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class JoinCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;

        if (args.length == 1) {
            QueueManager queueManager = new QueueManager();
            switch (args[0].toLowerCase()) {
                case "singles" -> {
                    queueManager.addToSinglesQueue(player);
                    checkQueueAndCreateGame(queueManager.getSinglesQueue());
                }
                case "doubles" -> {
                    queueManager.addToDoublesQueue(player);
                    checkQueueAndCreateGame(queueManager.getDoublesQueue());
                }
                default -> player.sendMessage("Invalid queue type!");
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 1) return List.of("singles", "doubles");
        return List.of();
    }

    private void checkQueueAndCreateGame(List<Player> queue) {
        if (queue.size() >= 1) {
            List<Player> players = List.of(queue.getFirst());
            new GameSession(players, getRandomMap(), HyroDuels.getInstance());
        }
    }

    private GameMap getRandomMap() {
        File mapsFolder = new File(HyroDuels.getInstance().getDataFolder(), "maps");
        File[] files = mapsFolder.listFiles();
        if (files == null || files.length == 0) return null;

        int random = (int) (Math.random() * files.length);
        File file = files[random];
        return new GameMap(file);
    }
}
