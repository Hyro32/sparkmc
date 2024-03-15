package dev.mardroide.bukkit;

import dev.mardroide.bukkit.commands.BanCommand;
import dev.mardroide.bukkit.commands.KickCommand;
import dev.mardroide.bukkit.commands.LangCommand;
import dev.mardroide.bukkit.commands.UnbanCommand;
import dev.mardroide.bukkit.listeners.AsyncChatListener;
import dev.mardroide.bukkit.listeners.CommandPreprocessListener;
import dev.mardroide.lib.jdbc.Database;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MegatronBukkit extends JavaPlugin {
    @Getter
    private static MegatronBukkit instance;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration configuration = getConfig();
        if (configuration != null) saveDefaultConfig();

        Database.connect(
                getConfig().getString("database.uri"),
                getConfig().getString("database.name")
        );

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);

        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("language").setExecutor(new LangCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Bukkit] Plugin enabled");
    }

    @Override
    public void onDisable() {
        Database.disconnect();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED  +"[Bukkit] Plugin disabled");
    }
}
