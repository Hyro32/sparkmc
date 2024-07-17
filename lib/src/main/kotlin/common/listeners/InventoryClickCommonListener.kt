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
            val menu: Menu = inventory.holder as Menu
            val items: MutableMap<Int, Item> = menu.items

            if (menu.clickeable.not()) {
                event.isCancelled = true
                handleClick(event.whoClicked as Player, items, event.slot)
                return
            }

            if (items.containsKey(event.slot)) {
                event.isCancelled = true
                handleClick(event.whoClicked as Player, items, event.slot)
            }
        }
    }

    private fun handleClick(player: Player, items: MutableMap<Int, Item>, slot: Int) {
        val item: Item = items[slot]!!
        item.let { it.consumer?.invoke(player) }
    }
}