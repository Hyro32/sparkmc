package one.hyro.spark.lobby;

import lombok.Getter;
import one.hyro.spark.lib.i18n.I18n;
import org.bukkit.plugin.java.JavaPlugin;

public class SparkLobby extends JavaPlugin {
    @Getter private static SparkLobby instance;

    @Override
    public void onEnable() {
        instance = this;
        I18n.setupInternationalization();
        getLogger().info("SparkLobby has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkLobby has been disabled!");
    }
}