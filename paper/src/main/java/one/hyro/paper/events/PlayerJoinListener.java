package one.hyro.paper.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.managers.TagsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();
        TagsManager.updateScoreboard(player);
        teleportPlayerToSpawn(player);
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
}
