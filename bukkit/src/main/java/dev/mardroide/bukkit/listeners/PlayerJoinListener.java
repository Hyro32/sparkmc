package dev.mardroide.bukkit.listeners;

import dev.mardroide.lib.jdbc.collections.PlayersCollection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        if (PlayersCollection.find(player.getUniqueId()) == null) {
            player.sendMessage("Welcome to the server, " + player.getName() + "!");
            PlayersCollection.create(player.getUniqueId());
        }
    }
}
