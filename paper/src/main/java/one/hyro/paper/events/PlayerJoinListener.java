package one.hyro.paper.events;

import one.hyro.paper.managers.BossbarsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BossbarsManager bossbarsManager = new BossbarsManager();
        bossbarsManager.showMainBossbar(event.getPlayer());
    }
}
