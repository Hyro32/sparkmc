package one.hyro.minigame

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID

class Session(private val minigame: Minigame, private val min: UByte, private val max: UByte) {
    val playersUuid: MutableList<UUID> = mutableListOf<UUID>()
    var state: SessionState = SessionState.WAITING
    var sessionWorld: SessionWorld = SessionWorld()

    init {
        if (min > max) throw IllegalArgumentException("Min players cannot be greater than max players.")
        minigame.waiting(this)
    }

    fun addPlayer(uuid: UUID, component: Component) {
        if (this.state != SessionState.WAITING) throw IllegalStateException("Cannot join a session that is not waiting.")
        val player: Player? = Bukkit.getPlayer(uuid)

        player?.let {
            // Teleport the player to the game map spawn location
            val location: Location = sessionWorld.world.spawnLocation ?: return@let
            it.teleportAsync(location).thenAccept { success ->
                if (!success) return@thenAccept
                playersUuid.add(uuid)

                for (playerUuid: UUID in playersUuid) {
                    val sessionPlayer: Player? = Bukkit.getPlayer(playerUuid)
                    sessionPlayer?.sendMessage(component) ?: continue
                }

                if (playersUuid.size.toUByte() >= min) setSessionState(SessionState.STARTING)
            }
        }
    }

    fun removePlayer(uuid: UUID, component: Component) {
        val player: Player? = Bukkit.getPlayer(uuid);

        player?.let {
            // Teleport the player to the lobby spawn location
            val location: Location = Bukkit.getWorld("world")?.spawnLocation ?: return@let
            it.teleportAsync(location)
        }

        for (playerUuid: UUID in playersUuid) {
            val sessionPlayer: Player? = Bukkit.getPlayer(playerUuid)
            sessionPlayer?.sendMessage(component) ?: continue
        }

        playersUuid.remove(uuid)
        val isStartingWithPlayers: Boolean = state == SessionState.STARTING && (playersUuid.size.toUByte() < min && playersUuid.isNotEmpty())
        if (isStartingWithPlayers) setSessionState(SessionState.WAITING)
        if (playersUuid.isEmpty()) setSessionState(SessionState.ENDING)
    }

    fun setSessionState(newState: SessionState) {
        state = newState
        when (newState) {
            SessionState.WAITING -> minigame.waiting(this)
            SessionState.STARTING -> minigame.starting(this)
            SessionState.PLAYING -> minigame.playing(this)
            SessionState.ENDING -> minigame.ending(this)
        }
    }

    fun isPlayerInSession(uuid: UUID): Boolean = playersUuid.contains(uuid)
}