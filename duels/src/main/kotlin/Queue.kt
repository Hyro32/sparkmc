package one.hyro

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.kits.Kit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object Queue {
    private val singlesQueue: MutableMap<UUID, Kit> = mutableMapOf()
    private val doublesQueue: MutableMap<UUID, Kit> = mutableMapOf()

    fun addToQueue(uuid: UUID, kit: Kit, isDoubles: Boolean) {
        if (isQueued(uuid)) {
            val queued: Player = Bukkit.getPlayer(uuid) ?: return
            queued.sendMessage(Component.text("You are already in the queue!", NamedTextColor.RED))
            return
        }
        if (isDoubles) doublesQueue[uuid] = kit else singlesQueue[uuid] = kit
    }

    fun getQueueByKit(kit: Kit, doubles: Boolean): List<UUID> {
        val queue = if (doubles) doublesQueue else singlesQueue
        return queue.filter { it.value == kit }.keys.toList()
    }

    fun removeFromQueue(player: UUID) = singlesQueue.remove(player) ?: doublesQueue.remove(player)
    fun isQueued(player: UUID): Boolean = singlesQueue.contains(player) || doublesQueue.contains(player)
}