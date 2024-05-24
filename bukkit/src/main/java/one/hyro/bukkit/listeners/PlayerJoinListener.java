package one.hyro.bukkit.listeners;

import one.hyro.bukkit.managers.ScoreboardManager;
import one.hyro.bukkit.managers.TablistManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        player.setScoreboard(ScoreboardManager.setScoreboard());
        TablistManager.setTablist(player);
    }
}
