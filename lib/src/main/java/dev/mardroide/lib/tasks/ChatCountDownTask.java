package dev.mardroide.lib.tasks;

import dev.mardroide.lib.i18n.I18n;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatCountDownTask {
    private JavaPlugin plugin;
    private Player[] players;
    private int seconds;

    public ChatCountDownTask(JavaPlugin plugin, Player[] players, int seconds) {
        this.plugin = plugin;
        this.players = players;
        this.seconds = seconds;
        start();
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (seconds > 0) {
                    for (Player player : players) {
                        String translation = I18n.getTranslation("en", "game.start.coundown");
                        player.sendMessage(String.format(translation, seconds));
                    }
                    seconds--;
                } else {
                    for (Player player : players) {
                        String locale = player.spigot().getLocale();
                        player.sendMessage(I18n.getTranslation("en", "game.start.started"));
                    }
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
