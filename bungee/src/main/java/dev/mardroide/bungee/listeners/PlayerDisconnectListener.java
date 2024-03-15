package dev.mardroide.bungee.listeners;

import dev.mardroide.lib.enums.DatabaseKeys;
import dev.mardroide.lib.jdbc.collections.PlayersCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;

public class PlayerDisconnectListener implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayersCollection.update(player.getUniqueId(), DatabaseKeys.LAST_JOIN, new Date());
    }
}
