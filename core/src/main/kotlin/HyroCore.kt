package one.hyro

import one.hyro.listeners.AsyncChatListener
import one.hyro.listeners.PlayerJoinListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class HyroCore: JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveResource("config.yml", false)
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