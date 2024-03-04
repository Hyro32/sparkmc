package dev.mardroide.bukkit.listeners;

import dev.mardroide.lib.database.collections.PlayersCollection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            player.sendMessage("Welcome to the server, " + player.getName() + "!");
            PlayersCollection.create(player.getUniqueId());
        }
    }
}
