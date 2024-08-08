package one.hyro

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import one.hyro.commands.EcoCommand
import one.hyro.common.listener.InventoryClickCommonListener
import one.hyro.listeners.BlockBreakListener
import one.hyro.listeners.BlockPlaceListener
import one.hyro.listeners.PlayerJumpListener
import one.hyro.listeners.PlayerToggleSneakListener
import one.hyro.recipes.Elevator
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class HyroSurvival: JavaPlugin() {
    override fun onEnable() {
        instance = this
        Lib.init(this)
        Elevator.registerRecipes()

        val manager: LifecycleEventManager<Plugin> = this.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands: Commands = event.registrar()
            commands.register("eco", EcoCommand)
        }

        Bukkit.getPluginManager().registerEvents(BlockBreakListener, this)
        Bukkit.getPluginManager().registerEvents(BlockPlaceListener, this)
        Bukkit.getPluginManager().registerEvents(InventoryClickCommonListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerJumpListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerToggleSneakListener, this)
        logger.info("HyroSurvival has been enabled!")
    }

    override fun onDisable() {
        logger.info("HyroSurvival has been disabled!")
    }

    // See https://kotlinlang.org/docs/coding-conventions.html#class-layout
    companion object {
        var instance: HyroSurvival? = null
    }
}