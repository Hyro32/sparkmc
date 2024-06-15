package one.hyro.paper.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.managers.TagsManager;
import one.hyro.paper.utilities.ConfigParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PlayerJoinListener implements Listener {
    private final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private final List<String> lobbyWorlds = config.getStringList("lobby-status.worlds");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();
        givePlayerItems(player);
        TagsManager.setCustomTags(player);
        teleportPlayerToSpawn(player);
    }

    private void teleportPlayerToSpawn(Player player) {
        player.teleportAsync(player.getWorld().getSpawnLocation()).thenAccept(success -> {
            if (success && player.hasPermission("hyro.welcome")) {
                Component welcomeMessage = player.displayName().append(Component.text(" has joined the server!", NamedTextColor.YELLOW));
                player.getServer().broadcast(welcomeMessage);
            }
        });
    }

    private void givePlayerItems(Player player) {
        if (lobbyWorlds.contains(player.getWorld().getName())) {
            player.getInventory().clear();

            File configFile = new File(HyrosPaper.getInstance().getDataFolder(), "default-items.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            Map<ItemStack, Integer> items = ConfigParser.parseItems(config, "default-items", player);

            for (Map.Entry<ItemStack, Integer> entry : items.entrySet()) {
                ItemStack item = entry.getKey();
                int slot = entry.getValue();
                player.getInventory().setItem(slot, item);
            }
        }
    }
}
