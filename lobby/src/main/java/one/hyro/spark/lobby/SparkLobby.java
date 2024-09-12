package one.hyro.spark.lobby;

import lombok.Getter;
import one.hyro.spark.lib.SparkLib;
import one.hyro.spark.lib.i18n.I18n;
import one.hyro.spark.lobby.listeners.PlayerJoinListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class SparkLobby extends JavaPlugin implements PluginMessageListener {
    @Getter private static SparkLobby instance;

    @Override
    public void onEnable() {
        instance = this;
        SparkLib.init(this);
        I18n.setupInternationalization();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        getLogger().info("SparkLobby has been enabled!");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        getLogger().info("SparkLobby has been disabled!");
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
    }
}