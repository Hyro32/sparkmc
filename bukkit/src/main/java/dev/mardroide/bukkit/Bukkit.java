package dev.mardroide.bukkit;

import dev.mardroide.bukkit.listeners.AsyncChatListener;
import dev.mardroide.bukkit.listeners.CommandPreprocessListener;
import dev.mardroide.bukkit.listeners.PlayerJoinListener;
import dev.mardroide.lib.enums.Collections;
import dev.mardroide.lib.database.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bukkit extends JavaPlugin {
    private static Bukkit instance;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration configuration = getConfig();
        if (configuration == null) saveDefaultConfig();

        Database.connect();
        Collections.createCollections();

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        System.out.println("[Bukkit] Plugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Bukkit] Plugin disabled");
    }

    public static Bukkit getInstance() {
        return instance;
    }
}
