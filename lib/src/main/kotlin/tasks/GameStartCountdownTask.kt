package one.hyro.tasks

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import one.hyro.minigame.Session
import one.hyro.minigame.SessionState
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable


class GameStartCountdownTask(plugin: Plugin, private val session: Session) : BukkitRunnable() {
    private var seconds: Int = 7
    init { runTaskTimer(plugin, 0, 20) }

    override fun run() {
        if (seconds == 0) {
            session.setSessionState(SessionState.PLAYING)
            cancel()
            return
        }

        if (seconds == 10 || seconds in 1..5) {
            session.playersUuid.forEach { uuid ->
                val player: Player? = Bukkit.getPlayer(uuid)
                player?.let {
                    val title: Title = Title.title(
                        Component.empty(),
                        Component.text(seconds, NamedTextColor.RED)
                    )

                    player.showTitle(title)
                }
            }
        }

        seconds--
    }
}