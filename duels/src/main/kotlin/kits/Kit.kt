package one.hyro.kits

import net.kyori.adventure.text.Component
import one.hyro.builder.inventory.CustomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface Kit {
    fun name(): Component
    fun description(): Component
    fun items(): List<CustomItem>
    fun helmet(): CustomItem? = null
    fun chestplate(): CustomItem? = null
    fun leggings(): CustomItem? = null
    fun boots(): CustomItem? = null
    fun shield(): Boolean = false

    private fun applyInventory(player: Player) {
        player.inventory.clear()
        player.inventory.helmet = helmet()?.item
        player.inventory.chestplate = chestplate()?.item
        player.inventory.leggings = leggings()?.item
        player.inventory.boots = boots()?.item
        if (shield()) player.inventory.setItemInOffHand(ItemStack(Material.SHIELD))
        items().forEach { player.inventory.addItem(it.item) }
    }
}
