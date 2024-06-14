package one.hyro.paper.events;

import one.hyro.paper.HyrosPaper;
import one.hyro.paper.utilities.ConfigParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.List;

public class PlayerInteractListener implements Listener {
    private final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private final List<String> lobbyWorlds = config.getStringList("lobby-status.worlds");

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().isRightClick() && lobbyWorlds.contains(player.getWorld().getName())) {
            File itemsFile = new File(HyrosPaper.getInstance().getDataFolder(), "default-items.yml");
            FileConfiguration itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);

            event.setCancelled(true);
            int slot = event.getPlayer().getInventory().getHeldItemSlot();
            ConfigParser.runItemCommands(itemsConfig, "default-items", slot, player);
        }
    }
}
