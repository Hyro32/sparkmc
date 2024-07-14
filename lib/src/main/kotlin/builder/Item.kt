package one.hyro.builder

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class Item {
    var item: ItemStack? = null
    var material: Material? = null
    var meta: ItemMeta? = null
    var consumer: ((Player) -> Unit)? = null

    constructor(material: Material) {
        this.material = material
        this.item = ItemStack(material)
        this.meta = item?.itemMeta
    }

    constructor(player: Player) {
        // TODO: Create item with player head
    }

    fun amount(amount: Int) = apply {
        item?.amount = amount
        return this
    }

    fun displayName(displayName: Component) = apply {
        meta?.displayName(displayName)
        item?.itemMeta = meta
        return this
    }

    fun lore(lore: Component) = apply {
        meta?.lore(listOf(lore))
        item?.itemMeta = meta
        return this
    }

    fun enchantment(enchantment: Enchantment, level: Int) = apply {
        item?.addEnchantment(enchantment, level)
        return this
    }

    fun hideEnchantments() = apply {
        item?.itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun click(consumer: (Player) -> Unit) = apply {
        this.consumer = consumer
        return this
    }

    fun build() = apply {
        item?.itemMeta = meta
        return this
    }
}