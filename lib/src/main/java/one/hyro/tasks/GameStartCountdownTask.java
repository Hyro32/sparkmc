package one.hyro.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameStartCountdownTask extends BukkitRunnable {
    private final GameSession session;
    private final Component countdown;
    private final Component start;
    private int seconds;

    public GameStartCountdownTask(Plugin plugin, GameSession session, int seconds, Component countdown, Component start) {
        this.session = session;
        this.seconds = seconds;
        this.countdown = countdown;
        this.start = start;
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        seconds--;

        if (session.getPlayersUuids().size() < session.getMinPlayers()) {
            this.cancel();
            session.setGameStatus(GameStatus.WAITING);
        }

        if (seconds <= 0) {
            this.cancel();
            for (UUID uuid : session.getPlayersUuids()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Location location = player.getLocation();
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                player.showTitle(Title.title(start, Component.empty()));
                player.sendMessage(start);
            }

            session.setGameStatus(GameStatus.IN_GAME);
        }

        if (seconds >= 1 && seconds <= 5) {
            Component title = Component.text(seconds, NamedTextColor.RED);
            Title titleMessage = Title.title(title, Component.empty());

            for (UUID uuid : session.getPlayersUuids()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Location location = player.getLocation();
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

                player.sendMessage(countdown);
                player.showTitle(titleMessage);
            }
        }
    }
}
