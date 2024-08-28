package one.hyro

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import one.hyro.commands.GamemodeCommand
import one.hyro.commands.MinigamesCommand
import one.hyro.common.listener.InventoryClickCommonListener
import one.hyro.listeners.AsyncChatListener
import one.hyro.listeners.LobbyListeners
import one.hyro.listeners.PlayerCommandPreprocessListener
import one.hyro.listeners.PlayerJoinListener
import one.hyro.managers.ScoreboardManager
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class HyroCore: JavaPlugin() {
    override fun onEnable() {
        instance = this
        Lib.init(this)
        saveResource("config.yml", false)
        ScoreboardManager.registerRoleTeams()
        ScoreboardManager.updateScoreboard(this)

        val manager: LifecycleEventManager<Plugin> = this.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands: Commands = event.registrar()
            commands.register("gamemode", GamemodeCommand)
            commands.register("minigames", MinigamesCommand)
        }

        Bukkit.getPluginManager().registerEvents(AsyncChatListener, this)
        Bukkit.getPluginManager().registerEvents(LobbyListeners, this)
        Bukkit.getPluginManager().registerEvents(InventoryClickCommonListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerCommandPreprocessListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinListener, this)
        logger.info("HyroCore enabled!")
    }

    override fun onDisable() {
        ScoreboardManager.unregisterRoleTeams()
        logger.info("HyroCore disabled!")
    }

    // See https://kotlinlang.org/docs/coding-conventions.html#class-layout
    companion object {
        var instance: HyroCore? = null
    }
}