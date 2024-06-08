package one.hyro.paper;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import one.hyro.paper.commands.KickCommand;
import one.hyro.paper.commands.PingCommand;
import one.hyro.paper.commands.ReloadCommand;
import one.hyro.paper.commands.GotoCommand;
import one.hyro.paper.events.LobbyPlayerStatusListener;
import one.hyro.paper.events.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyrosPaper extends JavaPlugin {
    private static HyrosPaper instance;

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml", false);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("goto", "Teleport to a player between servers", new GotoCommand());
            commands.register("kick", "Kick a player from the server", new KickCommand());
            commands.register("ping", "See you server latency", new PingCommand());
            commands.register("hreload", "Reload the plugin configuration", new ReloadCommand());
        });

        getServer().getPluginManager().registerEvents(new LobbyPlayerStatusListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(),this);

        Bukkit.getLogger().info("HyrosPaper has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HyrosPaper has been disabled!");
    }

    public static HyrosPaper getInstance() {
        return instance;
    }
}
