package one.hyro.builder.inventory

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import one.hyro.Lib
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class CustomItem(material: Material) {
    var item: ItemStack = ItemStack(material)
    var meta: ItemMeta? = item.itemMeta
    var interactionConsumer: ((Player) -> Unit)? = null
    var clickConsumer: ((Player) -> Unit)? = null

    fun amount(amount: Int) = apply {
        if (amount <= 0 || amount > 64) item.amount = 1 else item.amount = amount
        return this
    }

    fun displayName(displayName: Component) = apply {
        meta?.displayName(displayName.decoration(TextDecoration.ITALIC, false))
        item.itemMeta = meta
        return this
    }

    fun lore(lore: Component) = apply {
        meta?.lore(listOf(lore.decoration(TextDecoration.ITALIC, false)))
        item.itemMeta = meta
        return this
    }

    fun enchantment(enchantment: Enchantment, level: Int) = apply {
        item.addEnchantment(enchantment, level)
        return this
    }

    fun hideEnchantments() = apply {
        item.itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun interact(consumer: (Player) -> Unit) = apply {
        this.interactionConsumer = consumer
        return this
    }

    fun click(consumer: (Player) -> Unit) = apply {
        this.clickConsumer = consumer
        return this
    }

    fun persistentData(key: String, value: String) = apply {
        val namespacedKey = NamespacedKey(Lib.plugin, key)
        meta?.persistentDataContainer?.set(namespacedKey, PersistentDataType.STRING, value)
        return this
    }

    fun build() = apply {
        item.itemMeta = meta
        return this
    }
}