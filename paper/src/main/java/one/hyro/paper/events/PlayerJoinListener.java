package one.hyro.paper.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.managers.BossbarsManager;
import one.hyro.paper.utilities.Chalk;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerJoinListener implements Listener {
    private final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private final List<String> lobbyWorlds = config.getStringList("lobby-status.worlds");
    private final String header = config.getString("tablist.header");
    private final String footer = config.getString("tablist.footer");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        teleportPlayerToSpawn(player);
        givePlayerItems(player);
        setPlayerTablist(player);
    }

    private void teleportPlayerToSpawn(Player player) {
        player.teleportAsync(player.getWorld().getSpawnLocation()).thenAccept(success -> {
            if (success && player.hasPermission("hyro.welcome")) {
                Component welcomeMessage = Component.text(player.getDisplayName())
                        .append(Component.text(" has joined the server!", NamedTextColor.YELLOW));
                player.getServer().broadcast(welcomeMessage);
            }
        });
    }

    private void setPlayerTablist(Player player) {
        if (header == null || footer == null) return;
        player.sendPlayerListHeaderAndFooter(
                Component.text(Chalk.colorizeLegacy(header)),
                Component.text(Chalk.colorizeLegacy(footer))
        );
    }

    private void givePlayerItems(Player player) {
        if (lobbyWorlds.contains(player.getWorld().getName())) {
            player.getInventory().clear();

            ItemStack compass = new ItemStack(Material.COMPASS, 1);
            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.displayName(Component.text("Server Selector", NamedTextColor.GOLD));
            compass.setItemMeta(compassMeta);

            player.getInventory().setItem(0, compass);
        }
    }
}
