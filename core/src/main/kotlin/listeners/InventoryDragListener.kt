package one.hyro.listeners

import one.hyro.HyroCore
import one.hyro.builder.Menu
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

object InventoryDragListener: Listener {
    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val inventory: Inventory = event.inventory

        if (inventory.holder is Menu) {
            val menu: Menu = inventory.holder as Menu
            if (!menu.draggable) event.isCancelled = true
        }

        val lobbyWorlds: List<String> = HyroCore.instance?.config?.getStringList("lobby.worlds")?: emptyList()
        if (lobbyWorlds.contains(event.whoClicked.world.name)) event.isCancelled = true
    }
}