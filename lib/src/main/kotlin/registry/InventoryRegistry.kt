package one.hyro.registry

import one.hyro.builder.inventory.CustomItem
import org.bukkit.inventory.ItemStack

object InventoryRegistry {
    private val items: MutableList<CustomItem> = mutableListOf()

    fun registerItem(item: CustomItem) = items.add(item)
    fun unregisterItem(item: CustomItem) = items.remove(item)
    fun getItem(item: ItemStack) = items.find { it.item == item }
}