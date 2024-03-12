package dev.mardroide.bukkit;

import dev.mardroide.bukkit.commands.KickCommand;
import dev.mardroide.bukkit.commands.LangCommand;
import dev.mardroide.bukkit.listeners.AsyncChatListener;
import dev.mardroide.bukkit.listeners.CommandPreprocessListener;
import dev.mardroide.bukkit.listeners.PlayerJoinListener;
import dev.mardroide.lib.jdbc.Database;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bukkit extends JavaPlugin {
    @Getter
    private static Bukkit instance;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration configuration = getConfig();
        if (configuration == null) saveDefaultConfig();

        //Database.connect(getConfig().getString("database.uri"), getConfig().getString("database.name"));

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("language").setExecutor(new LangCommand());

        System.out.println("[Bukkit] Plugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Bukkit] Plugin disabled");
    }
}
