package one.hyro.spark.lobby;

import lombok.Getter;
import one.hyro.spark.lib.SparkLib;
import one.hyro.spark.lib.i18n.I18n;
import one.hyro.spark.lobby.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SparkLobby extends JavaPlugin {
    @Getter private static SparkLobby instance;

    @Override
    public void onEnable() {
        instance = this;
        SparkLib.init(this);
        I18n.setupInternationalization();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getLogger().info("SparkLobby has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkLobby has been disabled!");
    }
}