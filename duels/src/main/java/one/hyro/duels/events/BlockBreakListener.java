package one.hyro.duels.events;

import one.hyro.managers.BlockManager;
import one.hyro.managers.GameManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        BlockManager blockManager = BlockManager.getInstance();
        GameManager gameManager = GameManager.getInstance();

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (gameManager.isPlayerInGame(player.getUniqueId())) {
            if (!blockManager.getBlocks().containsKey(block)) event.setCancelled(true);
            else blockManager.removeBlock(block, gameManager.getGameSession(player.getUniqueId()));
        }
    }
}
