package dev.mardroide.bukkit.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChatListener implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        event.setFormat(ChatColor.GOLD + player.getDisplayName() + ": "+ ChatColor.GRAY + message);
    }
}
