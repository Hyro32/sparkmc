package dev.mardroide.bungee;

import dev.mardroide.bungee.configuration.BungeeConfiguration;
import dev.mardroide.bungee.listeners.PlayerDisconnectListener;
import dev.mardroide.bungee.listeners.PostLoginListener;
import dev.mardroide.lib.jdbc.Database;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class MegatronBungee extends Plugin {
    @Getter
    private static MegatronBungee instance;

    @Override
    public void onEnable() {
        instance = this;

        BungeeConfiguration.loadConfiguration();

        Database.connect(
                BungeeConfiguration.getConfiguration().getString("database.uri"),
                BungeeConfiguration.getConfiguration().getString("database.name")
        );

        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());

        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.GOLD + "[Bungee] Plugin enabled");
    }

    @Override
    public void onDisable() {
        Database.disconnect();
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.RED + "[Bungee] Plugin disabled");
    }
}
