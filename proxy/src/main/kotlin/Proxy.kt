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
import one.hyro.commands.MsgCommand
import one.hyro.commands.ReplyCommand
import org.slf4j.Logger

@Plugin(
    id = "proxy",
    name = "HyroProxy",
    version = "1.0.0-SNAPSHOT"
)
class Proxy @Inject constructor(val logger: Logger, val proxy: ProxyServer) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        instance = this
        val commandManager: CommandManager = proxy.commandManager

        val kickCommand: BrigadierCommand = KickCommand.createKickCommand(proxy)
        val kickMeta: CommandMeta = commandManager.metaBuilder("kick").build()

        val msgCommand: BrigadierCommand = MsgCommand.createMsgCommand(proxy)
        val msgMeta: CommandMeta = commandManager.metaBuilder("msg").build()

        val replyCommand: BrigadierCommand = ReplyCommand.createReplyCommand(proxy)
        val replyMeta: CommandMeta = commandManager.metaBuilder("reply").build()

        commandManager.register(kickMeta, kickCommand)
        commandManager.register(msgMeta, msgCommand)
        commandManager.register(replyMeta, replyCommand)
        logger.info("HyroProxy has been initialized!")
    }

    companion object {
        lateinit var instance: Proxy
    }
}