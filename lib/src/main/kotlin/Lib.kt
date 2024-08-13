package one.hyro

import one.hyro.minigame.Minigame
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Lib {
    lateinit var plugin: JavaPlugin

    fun init(plugin: JavaPlugin) {
        plugin.logger.info("Lib initialized.")
        this.plugin = plugin
        setup()
    }

    // Generate resources for the plugin.
    private fun setup() {
        if (plugin is Minigame) {
            val mapsFolder = File(Bukkit.getWorldContainer(), "maps")
            if (!mapsFolder.exists()) mapsFolder.mkdirs()
        }
    }
}