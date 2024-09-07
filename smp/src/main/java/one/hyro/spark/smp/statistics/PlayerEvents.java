package one.hyro.spark.smp.statistics;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerEvents implements Listener {

    private final PlayerStatisticsManager statsManager;

    public PlayerEvents(PlayerStatisticsManager statsManager) {
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        statsManager.updateDeaths(player);
        if (player.getKiller() != null) {
            statsManager.updateKills(player.getKiller());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Vector from = event.getFrom().toVector();
        Vector to = event.getTo().toVector();
        double distance = from.distance(to) / 1000; // Convert to kilometers
        statsManager.updateKilometersWalked(player, distance);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        // Check if the crafted item is a Netherite Ingot
        ItemStack result = event.getCurrentItem();
        if (result != null && result.getType() == Material.NETHERITE_INGOT) {
            Player player = (Player) event.getWhoClicked();
            statsManager.updateNetheriteIngotsCrafted(player);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_ORE || event.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            Player player = event.getPlayer();
            statsManager.updateDiamondsMined(player);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() != null && event.getTo().getWorld().getName().equals("the_end")) {
            Player player = event.getPlayer();
            statsManager.updateEndVisits(player);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Handle respawn logic if needed
    }
}
