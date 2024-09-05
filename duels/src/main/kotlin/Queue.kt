package one.hyro

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.instances.DuelSession
import one.hyro.common.Kit
import one.hyro.registry.SessionsRegistry
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object Queue {
    private val singlesQueue: MutableMap<UUID, Kit> = mutableMapOf()
    private val doublesQueue: MutableMap<UUID, Kit> = mutableMapOf()

    fun addPlayerToQueue(uuid: UUID, kit: Kit, isDoubles: Boolean) {
        if (isPlayerQueued(uuid)) {
            val queued: Player? = Bukkit.getPlayer(uuid)
            queued?.sendMessage(Component.text("You are already in the queue!", NamedTextColor.RED))
            return
        }

        if (isDoubles) doublesQueue[uuid] = kit else singlesQueue[uuid] = kit
        fillOrCreateSession(kit, isDoubles)
    }

    fun getQueueByKit(kit: Kit, isDoubles: Boolean): List<UUID> {
        val queue = if (isDoubles) doublesQueue else singlesQueue
        return queue.filter { it.value == kit }.keys.toList()
    }

    fun removePlayerFromQueue(player: UUID) = singlesQueue.remove(player) ?: doublesQueue.remove(player)
    fun isPlayerQueued(player: UUID): Boolean = singlesQueue.contains(player) || doublesQueue.contains(player)

    private fun fillOrCreateSession(kit: Kit, isDoubles: Boolean) {
        val uuid = if (isDoubles) doublesQueue.keys.first() else singlesQueue.keys.first()

        for (session in SessionsRegistry.sessions) {
            if (session !is DuelSession) continue;
            if (session.playersUuids.size < session.maxPlayers && session.kit == kit) {
                session.addPlayer(uuid, Component.text("You have joined the session!"))
                return
            }
        }

        val maxPlayers = if (isDoubles) 4 else 2
        val newDuelSession = DuelSession(HyroDuels.instance, 2, maxPlayers, kit)
        newDuelSession.addPlayer(uuid, Component.text("You have joined the session!"))
    }
}