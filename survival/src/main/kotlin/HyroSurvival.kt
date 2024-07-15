package one.hyro

import one.hyro.listeners.BlockPlaceListener
import one.hyro.listeners.PlayerJumpListener
import one.hyro.listeners.PlayerToggleSneakListener
import one.hyro.recipes.Elevator
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class HyroSurvival: JavaPlugin() {
    override fun onEnable() {
        instance = this
        Elevator().registerRecipes()

        Bukkit.getPluginManager().registerEvents(BlockPlaceListener, this)
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