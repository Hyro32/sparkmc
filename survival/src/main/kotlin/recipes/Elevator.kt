package one.hyro.recipes

import com.jeff_media.customblockdata.CustomBlockData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.HyroSurvival
import one.hyro.builder.Item
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class Elevator: Recipe {
    private val keyPrefix: String = "elevator_"
    private val key: NamespacedKey = NamespacedKey(HyroSurvival.instance!!, "elevator")

    fun goToAboveFloor(player: Player) {
        val location: Location = player.location
        for (y in location.blockY until location.world.maxHeight) {
            val block = location.world.getBlockAt(location.blockX, y, location.blockZ)

            if (isElevatorBlock(block)) {
                val yaw: Float = player.eyeLocation.yaw
                val pitch: Float = player.eyeLocation.pitch
                val aboveElevator = Location(player.world, block.x.toDouble(), block.y + 1.0, block.z.toDouble(), yaw, pitch)
                player.teleportAsync(aboveElevator.toCenterLocation()).thenAccept { success ->
                    if (!success) return@thenAccept
                    player.playSound(aboveElevator, Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f)
                }
            }
        }
    }

    fun goToBelowFloor(player: Player) {
        val location: Location = player.location
        for (y in location.blockY downTo location.world.minHeight) {
            val block = location.world.getBlockAt(location.blockX, y, location.blockZ)

            if (isElevatorBlock(block)) {
                val yaw: Float = player.eyeLocation.yaw
                val pitch: Float = player.eyeLocation.pitch
                val belowElevator = Location(player.world, block.x.toDouble(), block.y + 1.0, block.z.toDouble(), yaw, pitch)
                player.teleportAsync(belowElevator.toCenterLocation()).thenAccept { success ->
                    if (!success) return@thenAccept
                    player.playSound(belowElevator, Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f)
                }
            }
        }
    }

    fun isElevatorBlock(block: Block): Boolean {
        val container: PersistentDataContainer = CustomBlockData(block, HyroSurvival.instance!!)
        return container.has(key, PersistentDataType.STRING)
    }

    fun dropElevatorByColor(player: Player, block: Block, color: Material) {
        removeElevatorData(block)

        val elevatorItem: Item = Item(color)
            .displayName(Component.text("Elevator", NamedTextColor.GOLD))
            .lore(Component.text("Jump to go up and sneak to go down", NamedTextColor.GRAY))
            .persistentData("elevator", "elevator", HyroSurvival.instance!!)
            .build()

        player.world.dropItemNaturally(block.location, elevatorItem.item!!)
    }

    private fun removeElevatorData(block: Block) {
        val container: PersistentDataContainer = CustomBlockData(block, HyroSurvival.instance!!)
        container.remove(key)
    }

    override fun registerRecipes() {
        val colors = listOf(
            Material.WHITE_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.GRAY_CONCRETE,
            Material.BLACK_CONCRETE, Material.BROWN_CONCRETE, Material.RED_CONCRETE,
            Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE,
            Material.GREEN_CONCRETE, Material.CYAN_CONCRETE, Material.LIGHT_BLUE_CONCRETE,
            Material.BLUE_CONCRETE, Material.PURPLE_CONCRETE, Material.MAGENTA_CONCRETE,
            Material.PINK_CONCRETE
        )

        colors.forEach { color ->
            val elevatorItem = Item(color)
                .displayName(Component.text("Elevator", NamedTextColor.GOLD))
                .lore(Component.text("Jump to go up and sneak to go down", NamedTextColor.GRAY))
                .persistentData("elevator", "elevator", HyroSurvival.instance!!)
                .build()

            val recipeKey = NamespacedKey(HyroSurvival.instance!!, keyPrefix + color.name.lowercase())
            val recipe = ShapedRecipe(recipeKey, elevatorItem.item!!)
            recipe.shape("AAA", "ABA", "ACA")
            recipe.setIngredient('A', color)
            recipe.setIngredient('B', Material.ENDER_PEARL)
            recipe.setIngredient('C', Material.REDSTONE_TORCH)

            Bukkit.addRecipe(recipe)
        }
    }
}