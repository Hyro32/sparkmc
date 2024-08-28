package one.hyro

import com.google.inject.Inject
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.util.UTF8ResourceBundleControl
import one.hyro.commands.*
import one.hyro.listeners.DisconnectListener
import one.hyro.listeners.LoginListener
import org.slf4j.Logger
import java.util.Locale
import java.util.ResourceBundle

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

        val banCommand: BrigadierCommand = BanCommand.createBanCommand(proxy)
        val banMeta: CommandMeta = commandManager.metaBuilder("ban").build()

        val kickCommand: BrigadierCommand = KickCommand.createKickCommand(proxy)
        val kickMeta: CommandMeta = commandManager.metaBuilder("kick").build()

        val msgCommand: BrigadierCommand = MsgCommand.createMsgCommand(proxy)
        val msgMeta: CommandMeta = commandManager.metaBuilder("msg").build()

        val replyCommand: BrigadierCommand = ReplyCommand.createReplyCommand(proxy)
        val replyMeta: CommandMeta = commandManager.metaBuilder("reply").aliases("r").build()

        val tempBanCommand: BrigadierCommand = TempBanCommand.createTempBanCommand(proxy)
        val tempBanMeta: CommandMeta = commandManager.metaBuilder("tempban").aliases("tban").build()

        commandManager.register(banMeta, banCommand)
        commandManager.register(kickMeta, kickCommand)
        commandManager.register(msgMeta, msgCommand)
        commandManager.register(replyMeta, replyCommand)
        commandManager.register(tempBanMeta, tempBanCommand)

        proxy.eventManager.register(this, DisconnectListener)
        proxy.eventManager.register(this, LoginListener)

        val registry: TranslationRegistry = TranslationRegistry.create(Key.key("proxy:i18n"))
        val bundleEN: ResourceBundle = ResourceBundle.getBundle("translations.en", Locale.ENGLISH, UTF8ResourceBundleControl.get())
        val bundleES: ResourceBundle = ResourceBundle.getBundle("translations.es", Locale.forLanguageTag("es"), UTF8ResourceBundleControl.get())
        registry.registerAll(Locale.ENGLISH, bundleEN, true)
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true)
        GlobalTranslator.translator().addSource(registry)
        logger.info("HyroProxy has been initialized!")
    }

    // See https://kotlinlang.org/docs/coding-conventions.html#class-layout
    companion object {
        lateinit var instance: Proxy
    }
}