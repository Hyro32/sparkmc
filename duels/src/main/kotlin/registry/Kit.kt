package one.hyro.registry

import one.hyro.builder.inventory.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

enum class Kit(
    val helmet: CustomItem,
    val chestplate: CustomItem,
    val leggings: CustomItem,
    val boots: CustomItem,
    val extra: CustomItem?,
    val inventory: List<CustomItem>,
) {
    CLASSIC(
        CustomItem(Material.DIAMOND_HELMET).enchantment(Enchantment.PROTECTION, 2).build(),
        CustomItem(Material.DIAMOND_CHESTPLATE).enchantment(Enchantment.PROTECTION, 2).build(),
        CustomItem(Material.DIAMOND_LEGGINGS).enchantment(Enchantment.PROTECTION, 2).build(),
        CustomItem(Material.DIAMOND_BOOTS).enchantment(Enchantment.PROTECTION, 2).build(),
        null,
        listOf(
            CustomItem(Material.DIAMOND_SWORD).enchantment(Enchantment.SHARPNESS, 2).build(),
            CustomItem(Material.BOW).build(),
            CustomItem(Material.ARROW).amount(16).build(),
            CustomItem(Material.GOLDEN_APPLE).amount(5).build(),
        ),
    ),
    UPDATED(
        CustomItem(Material.IRON_HELMET).build(),
        CustomItem(Material.IRON_CHESTPLATE).build(),
        CustomItem(Material.IRON_LEGGINGS).build(),
        CustomItem(Material.IRON_BOOTS).build(),
        CustomItem(Material.SHIELD).build(),
        listOf(
            CustomItem(Material.DIAMOND_SWORD).enchantment(Enchantment.SHARPNESS, 2).build(),
            CustomItem(Material.SHIELD).build(),
            CustomItem(Material.CROSSBOW).build(),
            CustomItem(Material.ARROW).amount(16).build(),
            CustomItem(Material.GOLDEN_APPLE).amount(5).build(),
        ),
    );

    fun applyInventory(player: Player) {
        player.inventory.helmet = helmet.item
        player.inventory.chestplate = chestplate.item
        player.inventory.leggings = leggings.item
        player.inventory.boots = boots.item
        if (extra != null) player.inventory.setItemInOffHand(extra.item)
        inventory.forEach { player.inventory.addItem(it.item) }
    }
}