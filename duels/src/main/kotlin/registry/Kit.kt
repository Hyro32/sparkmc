package one.hyro.registry

import one.hyro.builder.Item
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

enum class Kit(
    val helmet: Item,
    val chestplate: Item,
    val leggings: Item,
    val boots: Item,
    val extra: Item?,
    val inventory: List<Item>,
) {
    CLASSIC(
        Item(Material.DIAMOND_HELMET).enchantment(Enchantment.PROTECTION, 2).build(),
        Item(Material.DIAMOND_CHESTPLATE).enchantment(Enchantment.PROTECTION, 2).build(),
        Item(Material.DIAMOND_LEGGINGS).enchantment(Enchantment.PROTECTION, 2).build(),
        Item(Material.DIAMOND_BOOTS).enchantment(Enchantment.PROTECTION, 2).build(),
        null,
        listOf(
            Item(Material.DIAMOND_SWORD).enchantment(Enchantment.SHARPNESS, 2).build(),
            Item(Material.BOW).build(),
            Item(Material.ARROW).amount(16).build(),
            Item(Material.GOLDEN_APPLE).amount(5).build(),
        ),
    ),
    UPDATED(
        Item(Material.IRON_HELMET).build(),
        Item(Material.IRON_CHESTPLATE).build(),
        Item(Material.IRON_LEGGINGS).build(),
        Item(Material.IRON_BOOTS).build(),
        Item(Material.SHIELD).build(),
        listOf(
            Item(Material.DIAMOND_SWORD).enchantment(Enchantment.SHARPNESS, 2).build(),
            Item(Material.SHIELD).build(),
            Item(Material.CROSSBOW).build(),
            Item(Material.ARROW).amount(16).build(),
            Item(Material.GOLDEN_APPLE).amount(5).build(),
        ),
    );

    fun applyInventory(player: Player) {
        player.inventory.helmet = helmet.item
        player.inventory.chestplate = chestplate.item
        player.inventory.leggings = leggings.item
        player.inventory.boots = boots.item
        if (extra != null) player.inventory.setItemInOffHand(extra.item)
        inventory.forEach { player.inventory.addItem(it.item!!) }
    }
}