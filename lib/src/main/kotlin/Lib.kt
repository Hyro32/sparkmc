package one.hyro

import org.bukkit.plugin.java.JavaPlugin

object Lib {
    lateinit var plugin: JavaPlugin

    fun init(plugin: JavaPlugin) {
        plugin.logger.info("Lib initialized.")
        this.plugin = plugin
    }
}