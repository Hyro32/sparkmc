package one.hyro

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import one.hyro.commands.GamemodeCommand
import one.hyro.listeners.AsyncChatListener
import one.hyro.listeners.PlayerJoinListener
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class HyroCore: JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveResource("config.yml", false)

        val manager: LifecycleEventManager<Plugin> = this.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands: Commands = event.registrar()
            commands.register("gamemode", listOf("gm"), GamemodeCommand)
        }

        Bukkit.getPluginManager().registerEvents(AsyncChatListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinListener, this)
        logger.info("HyroCore enabled!")
    }

    override fun onDisable() {
        logger.info("HyroCore disabled!")
    }

    // See https://kotlinlang.org/docs/coding-conventions.html#class-layout
    companion object {
        var instance: HyroCore? = null
    }
}