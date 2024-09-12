package one.hyro.spark.smp;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import one.hyro.spark.lib.SparkLib;
import one.hyro.spark.lib.i18n.I18n;
import one.hyro.spark.smp.commands.HomeCommand;
import one.hyro.spark.smp.commands.RtpCommand;
import one.hyro.spark.smp.commands.ShopCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SparkSmp extends JavaPlugin {
    @Getter private static SparkSmp instance;

    @Override
    public void onEnable() {
        instance = this;
        SparkLib.init(this);
        I18n.setupInternationalization();

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(HomeCommand.createBrigadierCommand(), "Manage your homes");
            commands.register(RtpCommand.createBrigadierCommand(), "Teleports you to a random place", List.of("randomtp"));
            commands.register(ShopCommand.createBrigadierCommand(), "Open the smp items shop");
        });

        getLogger().info("SparkSmp has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SparkSmp has been disabled!");
    }
}