package one.hyro.spark.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import one.hyro.spark.lib.i18n.I18n;
import one.hyro.spark.proxy.commands.BanCommand;
import one.hyro.spark.proxy.commands.KickCommand;
import one.hyro.spark.proxy.commands.TempBanCommand;
import one.hyro.spark.proxy.commands.UnbanCommand;
import org.slf4j.Logger;

@Plugin(
        id = "spark-proxy",
        name = "SparkProxy",
        version = "0.0.1",
        description = "A simple velocity plugin to manage the sparkmc proxy",
        url = "https://sparkmc.org",
        authors = {"Hyro32"}
)
public class SparkProxy {
    private final ProxyServer proxy;
    private final Logger logger;

    @Inject
    public SparkProxy(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerCommands();
        I18n.setupInternationalization();
        logger.info("SparkProxy has been initialized!");
    }

    private void registerCommands() {
        // Register /ban command
        BrigadierCommand banCommand = BanCommand.createBrigadierCommand(proxy);
        proxy.getCommandManager().register(banCommand);

        // Register /tempban command
        BrigadierCommand tempBanCommand = TempBanCommand.createBrigadierCommand(proxy);
        proxy.getCommandManager().register(tempBanCommand);

        // Register /kick command
        BrigadierCommand kickCommand = KickCommand.createBrigadierCommand(proxy);
        proxy.getCommandManager().register(kickCommand);

        // Register /unban command
        BrigadierCommand unbanCommand = UnbanCommand.createBrigadierCommand(proxy);
        proxy.getCommandManager().register(unbanCommand);

        logger.info("All commands have been registered.");
    }

}
