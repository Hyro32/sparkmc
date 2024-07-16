package one.hyro.common.listeners

import one.hyro.builder.Item
import one.hyro.builder.Menu
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object InventoryClickCommonListener: Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory: Inventory = event.inventory

        if (inventory.holder is Menu) {
            event.isCancelled = true
            val menu: Menu = inventory.holder as Menu
            val item: Item? = menu.items[event.slot]
            item.let { it?.consumer?.invoke(event.whoClicked as Player) }
        }
    }
}