package one.hyro

import com.google.inject.Inject
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import one.hyro.commands.KickCommand
import org.slf4j.Logger

@Plugin(
    id = "proxy",
    name = "HyroProxy",
    version = "1.0.0-SNAPSHOT"
)
class Proxy @Inject constructor(val logger: Logger, val proxy: ProxyServer) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val commandManager: CommandManager = proxy.commandManager

        val kickCommand: BrigadierCommand = KickCommand.createKickCommand(proxy)
        val kickMeta: CommandMeta = commandManager.metaBuilder("kick").build()

        commandManager.register(kickMeta, kickCommand)
        logger.info("HyroProxy has been initialized!")
    }
}