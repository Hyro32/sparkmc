package one.hyro.duels.events;

import one.hyro.instances.GameSession;
import one.hyro.managers.BlockManager;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        BlockManager blockManager = BlockManager.getInstance();
        GameManager gameManager = GameManager.getInstance();

        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (gameManager.isPlayerInGame(playerUuid)) {
            GameSession session = gameManager.getGameSession(playerUuid);
            blockManager.addBlock(event.getBlock(), session);
        }
    }
}
