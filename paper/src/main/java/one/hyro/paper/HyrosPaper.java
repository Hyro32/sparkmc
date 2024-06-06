package one.hyro.paper;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import one.hyro.paper.managers.CommandsManager;
import one.hyro.paper.managers.EventsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class HyrosPaper extends JavaPlugin implements PluginBootstrap {
    private static HyrosPaper instance;

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml", false);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        new EventsManager().registerEvents(this);
        Bukkit.getLogger().info("HyrosPaper has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HyrosPaper has been disabled!");
    }

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        new CommandsManager(context).registerCommands();
    }

    public static HyrosPaper getInstance() {
        return instance;
    }
}
