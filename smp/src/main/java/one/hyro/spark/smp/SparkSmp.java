package one.hyro.spark.smp;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import one.hyro.spark.lib.SparkLib;
import one.hyro.spark.lib.i18n.I18n;
import one.hyro.spark.smp.commands.*;
import one.hyro.spark.smp.managers.HomeGUIManager;
import one.hyro.spark.smp.managers.HomeManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class SparkSmp extends JavaPlugin {
    @Getter private static SparkSmp instance;
    private HomeManager homeManager;

    @Override
    public void onEnable() {
        instance = this;
        SparkLib.init(this);
        I18n.setupInternationalization();

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File dbFile = new File(dataFolder, "homes.db");

        homeManager = new HomeManager(dbFile);
        new HomeGUIManager(homeManager);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(RtpCommand.createBrigadierCommand(), "Teleports you to a random place", List.of("randomtp"));
            commands.register(SpawnCommand.createBrigadierCommand(), "Teleports you to the spawn of the world", List.of("worldspawn"));
            commands.register(SetSpawnCommand.createBrigadierCommand(), "Sets the worlds spawn", List.of());

            commands.register(HomeCommand.createBrigadierCommand(homeManager), "Teleports you to your home", List.of());
            commands.register(HomesCommand.createBrigadierCommand(homeManager), "Opens Homes GUI", List.of());
            commands.register(SetHomeCommand.createBrigadierCommand(homeManager), "Sets home at your position", List.of());
            commands.register(DelHomeCommand.createBrigadierCommand(homeManager), "Deletes selected home", List.of());

        });

        getLogger().info("SparkSmp has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkSmp has been disabled!");
    }
}