package one.hyro.tasks

import one.hyro.minigame.Session
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class GameEndCountdownTask(plugin: Plugin, private val session: Session) : BukkitRunnable() {
    private var seconds: Int = 10
    init { runTaskTimer(plugin, 0, 20) }

    override fun run() {
        if (seconds == 0) {
            for (uuid in session.playersUuid) {
                val player: Player = Bukkit.getPlayer(uuid)?: continue
                val location: Location? = Bukkit.getWorld("world")?.spawnLocation
                player.teleportAsync(location!!)
            }

            session.sessionWorld.unload()
            cancel()
            return
        }

        seconds--
    }
}