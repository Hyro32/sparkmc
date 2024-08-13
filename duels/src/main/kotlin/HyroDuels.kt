package one.hyro

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import one.hyro.commands.JoinCommand
import one.hyro.commands.LeaveCommand
import one.hyro.common.listener.InventoryClickCommonListener
import one.hyro.common.listener.PlayerInteractCommonListener
import one.hyro.listeners.EntityDamageByEntityListener
import one.hyro.listeners.EntityDamageListener
import one.hyro.listeners.PlayerQuitListener
import one.hyro.minigame.Minigame
import one.hyro.minigame.Session
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class HyroDuels: JavaPlugin(), Minigame {
    override fun onEnable() {
        instance = this
        Lib.init(this)
        saveResource("config.yml", false)

        val mapsFolder = File(Bukkit.getWorldContainer(), "maps")
        if (!mapsFolder.exists()) mapsFolder.mkdirs()

        val manager: LifecycleEventManager<Plugin> = this.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands: Commands = event.registrar()
            commands.register("join", JoinCommand)
            commands.register("leave", LeaveCommand)
        }

        Bukkit.getPluginManager().registerEvents(EntityDamageByEntityListener, this)
        Bukkit.getPluginManager().registerEvents(EntityDamageListener, this)
        Bukkit.getPluginManager().registerEvents(InventoryClickCommonListener, this)
        Bukkit.getPluginManager().registerEvents(PlayerQuitListener, this)
        logger.info("HyroDuels has been enabled!")
    }

    override fun onDisable() {
        logger.info("HyroDuels has been disabled!")
    }

    override fun waiting(session: Session) {
        TODO("Not yet implemented")
    }

    override fun starting(session: Session) {
        TODO("Not yet implemented")
    }

    override fun playing(session: Session) {
        TODO("Not yet implemented")
    }

    override fun ending(session: Session) {
        TODO("Not yet implemented")
    }

    // See https://kotlinlang.org/docs/coding-conventions.html#class-layout
    companion object {
        var instance: HyroDuels? = null
    }
}