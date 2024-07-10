package one.hyro

import one.hyro.listeners.AsyncChatListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class HyroCore: JavaPlugin() {
    override fun onEnable() {
        saveResource("config.yml", false)
        Bukkit.getPluginManager().registerEvents(AsyncChatListener, this)
        logger.info("HyroCore enabled!")
    }

    override fun onDisable() {
        logger.info("HyroCore disabled!")
    }
}