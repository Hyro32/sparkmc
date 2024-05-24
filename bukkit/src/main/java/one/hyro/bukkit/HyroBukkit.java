package one.hyro.bukkit;

import one.hyro.bukkit.commands.BanCommand;
import one.hyro.bukkit.commands.KickCommand;
import one.hyro.bukkit.commands.LangCommand;
import one.hyro.bukkit.commands.UnbanCommand;
import one.hyro.bukkit.listeners.AsyncChatListener;
import one.hyro.bukkit.listeners.CommandPreprocessListener;
import one.hyro.bukkit.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyroBukkit extends JavaPlugin {
    private static HyroBukkit instance;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration configuration = getConfig();
        if (configuration != null) saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("language").setExecutor(new LangCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Bukkit] Plugin enabled");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED  +"[Bukkit] Plugin disabled");
    }

    public static HyroBukkit getInstance() {
        return instance;
    }
}
