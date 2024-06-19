package one.hyro.duels.events;

import one.hyro.instances.GameSession;
import one.hyro.managers.BlockManager;
import one.hyro.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        BlockManager blockManager = new BlockManager();
        GameManager gameManager = new GameManager();

        if (gameManager.isInGame(event.getPlayer())) {
            GameSession session = gameManager.getGameSession(event.getPlayer());
            blockManager.addBlock(event.getBlock(), session);
        }
    }
}
