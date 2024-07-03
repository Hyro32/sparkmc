package one.hyro.paper.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyroPaper;
import one.hyro.utils.Teleport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();
        Teleport.teleportToLobby(player, null);

        if (player.hasPermission("hyro.welcome")) {
            Component welcomeMessage = Component.translatable(
                    "info.player.join",
                    player.displayName()
            ).color(NamedTextColor.YELLOW);
            player.getWorld().forEachAudience(audience -> audience.sendMessage(welcomeMessage));
        }

        HyroPaper.getScoreboardManager().setCustomMainTab(player);
    }
}
