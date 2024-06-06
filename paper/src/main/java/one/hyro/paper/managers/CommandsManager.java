package one.hyro.paper.managers;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import one.hyro.paper.commands.PingCommand;
import one.hyro.paper.commands.TeleportCommand;

public class CommandsManager {
    private BootstrapContext context;

    public CommandsManager(BootstrapContext context) {
        this.context = context;
    }

    public void registerCommands() {
        LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("ping", "See you server latency", new PingCommand());
            commands.register("hreload", "Reload the plugin configuration", new TeleportCommand());
            commands.register("teleport", "Teleport to a player between servers", new TeleportCommand());
        });
    }
}
