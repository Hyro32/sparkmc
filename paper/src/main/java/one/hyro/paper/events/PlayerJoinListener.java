package one.hyro.paper.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.builders.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();
        teleportPlayerToSpawn(player);
        givePlayerItems(player);
    }

    private void teleportPlayerToSpawn(Player player) {
        player.teleportAsync(player.getWorld().getSpawnLocation()).thenAccept(success -> {
            if (success && player.hasPermission("hyro.welcome")) {
                Component welcomeMessage = Component.translatable(
                        "info.player.join",
                        player.displayName()
                ).color(NamedTextColor.YELLOW);
                player.getServer().broadcast(welcomeMessage);
            }
        });
    }

    private void givePlayerItems(Player player) {
        CustomItem compass = new CustomItem(Material.COMPASS)
                .setCustomId("navigator")
                .setDisplayName(Component.text("Minigames selector", NamedTextColor.GOLD))
                .onClick(clicker -> {
                    clicker.performCommand("minigames");
                })
                .build();

        CustomItem playerHead = new CustomItem(player)
                .setCustomId("player-head")
                .setDisplayName(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE))
                .build();


        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, compass.getItem());
        inventory.setItem(1, playerHead.getItem());
    }
}
