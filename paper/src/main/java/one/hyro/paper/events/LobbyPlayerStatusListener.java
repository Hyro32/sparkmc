package one.hyro.paper.events;

import one.hyro.paper.HyrosPaper;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class LobbyPlayerStatusListener implements Listener {
    private final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private final boolean isDamageDisabled = config.getBoolean("lobby-status.disable-damage");
    private final boolean isHungerDisabled = config.getBoolean("lobby-status.disable-hunger");
    private final boolean isBlocksDisabled = config.getBoolean("lobby-status.disable-blocks");
    private final List<String> lobbyWorlds = config.getStringList("lobby-status.worlds");

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!isLobbyWorld(player.getWorld().getName())) return;
        if (isBlocksDisabled) player.setGameMode(GameMode.ADVENTURE);
        if (isDamageDisabled) player.setInvulnerable(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (!isLobbyWorld(player.getWorld().getName())) return;
        if (isHungerDisabled) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
    }

    private boolean isLobbyWorld(String worldName) {
        return lobbyWorlds.contains(worldName);
    }
}
