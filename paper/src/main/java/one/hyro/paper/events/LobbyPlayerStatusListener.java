package one.hyro.paper.events;

import io.papermc.paper.event.player.PlayerPickItemEvent;
import one.hyro.paper.HyroPaper;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class LobbyPlayerStatusListener implements Listener {
    private final FileConfiguration config = HyroPaper.getInstance().getConfig();
    private final boolean isDamageDisabled = config.getBoolean("lobby-status.damage");
    private final boolean isHungerDisabled = config.getBoolean("lobby-status.hunger");
    private final boolean isBlocksDisabled = config.getBoolean("lobby-status.blocks");
    private final boolean isItemDropDisabled = config.getBoolean("lobby-status.item-drop");
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

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!isLobbyWorld(event.getPlayer().getWorld().getName())) return;
        if (isItemDropDisabled) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickItemEvent event) {
        if (!isLobbyWorld(event.getPlayer().getWorld().getName())) return;
        event.setCancelled(true);
    }

    private boolean isLobbyWorld(String worldName) {
        return lobbyWorlds.contains(worldName);
    }
}
