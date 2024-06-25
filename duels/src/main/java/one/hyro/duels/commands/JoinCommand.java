package one.hyro.duels.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import one.hyro.builders.CustomItem;
import one.hyro.builders.GameMenu;
import one.hyro.duels.HyroDuels;
import one.hyro.duels.enums.DuelMode;
import one.hyro.duels.managers.QueueManager;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameMap;
import one.hyro.instances.GameSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class JoinCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;
        if (args.length != 1) return;
        QueueManager queueManager = new QueueManager();

        GameMenu modeMenu = new GameMenu()
                .setCustomId("duels-mode-menu")
                .setTitle(Component.text("Choose your kit"))
                .setSize(27)
                .setHolder(player);

        switch (args[0].toLowerCase()) {
            case "singles" -> {
                CustomItem classic = new CustomItem(Material.DIAMOND_HELMET)
                        .setCustomId("classic")
                        .setDisplayName(Component.text("Classic"))
                        .amount(queueManager.getPlayersInSingleQueueByMode(DuelMode.CLASSIC).size())
                        .onClick(clicker -> {
                            clicker.closeInventory();
                            queueManager.addPlayerToSingleQueue(clicker, DuelMode.CLASSIC);
                            clicker.sendMessage(Component.text("You have been added to the queue!"));
                            checkSinglesQueueAndCreateGame(queueManager, DuelMode.CLASSIC);
                        })
                        .build();

                CustomItem bow = new CustomItem(Material.BOW)
                        .setCustomId("bow")
                        .setDisplayName(Component.text("Bow"))
                        .amount(queueManager.getPlayersInSingleQueueByMode(DuelMode.BOW).size())
                        .onClick(clicker -> {
                            clicker.closeInventory();
                            queueManager.addPlayerToSingleQueue(clicker, DuelMode.BOW);
                            clicker.sendMessage(Component.text("You have been added to the queue!"));
                            checkSinglesQueueAndCreateGame(queueManager, DuelMode.BOW);
                        })
                        .build();

                modeMenu.setItem(10, classic);
                modeMenu.setItem(11, bow);
            }
            case "doubles" -> {
                CustomItem classic = new CustomItem(Material.DIAMOND_HELMET)
                        .setCustomId("classic-doubles")
                        .setDisplayName(Component.text("Classic Doubles"))
                        .amount(queueManager.getPlayersInDoubleQueueByMode(DuelMode.CLASSIC).size())
                        .onClick(clicker -> {
                            clicker.closeInventory();
                            queueManager.addPlayerToDoubleQueue(clicker, DuelMode.CLASSIC);
                            clicker.sendMessage(Component.text("You have been added to the queue!"));
                            checkDoublesQueueAndCreateGame(queueManager, DuelMode.CLASSIC);
                        })
                        .build();

                CustomItem bow = new CustomItem(Material.BOW)
                        .setCustomId("bow-doubles")
                        .setDisplayName(Component.text("Bow Doubles"))
                        .amount(queueManager.getPlayersInDoubleQueueByMode(DuelMode.BOW).size())
                        .onClick(clicker -> {
                            clicker.closeInventory();
                            queueManager.addPlayerToDoubleQueue(clicker, DuelMode.BOW);
                            clicker.sendMessage(Component.text("You have been added to the queue!"));
                            checkDoublesQueueAndCreateGame(queueManager, DuelMode.BOW);
                        })
                        .build();

                modeMenu.setItem(10, classic);
                modeMenu.setItem(11, bow);
            }
            default -> player.sendMessage(Component.text("Invalid mode!"));
        }

        modeMenu.build();
        player.openInventory(modeMenu.getInventory());
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 1) return List.of("singles", "doubles");
        return List.of();
    }

    private void checkSinglesQueueAndCreateGame(QueueManager manager, DuelMode mode) {
        List<Player> queuePlayerList = manager.getPlayersInSingleQueueByMode(mode);
        if (queuePlayerList.size() >= 1) { // Change this to 2, is 1 for testing purposes
            List<Player> players = queuePlayerList.subList(0, 1);
            new GameSession(players, new GameMap(), HyroDuels.getInstance());
        }
    }

    private void checkDoublesQueueAndCreateGame(QueueManager manager, DuelMode mode) {
        List<Player> queuePlayerList = manager.getPlayersInDoubleQueueByMode(mode);
        if (queuePlayerList.size() >= 1) { // Change this to 2, is 1 for testing purposes
            List<Player> players = queuePlayerList.subList(0, 1);
            new GameSession(players, new GameMap(), HyroDuels.getInstance());
        }
    }
}
