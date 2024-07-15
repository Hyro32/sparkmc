package one.hyro.listeners

import com.jeff_media.customblockdata.CustomBlockData
import one.hyro.HyroSurvival
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object BlockPlaceListener: Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val itemInHand: ItemStack = event.itemInHand
        val container: PersistentDataContainer = itemInHand.itemMeta.persistentDataContainer
        val key: NamespacedKey = NamespacedKey(HyroSurvival.instance!!, "elevator")

        if (container.has(key, PersistentDataType.STRING)) {
            val blocksContainer: PersistentDataContainer = CustomBlockData(event.block, HyroSurvival.instance!!)
            blocksContainer.set(key, PersistentDataType.STRING, "elevator-" + event.block.type.name)
        }
    }
}