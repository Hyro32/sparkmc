package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameSession;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BlockManager {
    private static BlockManager instance;
    private final Map<Block, GameSession> blocks;

    private BlockManager() {
        this.blocks = new HashMap<>();
    }

    public void addBlock(Block block, GameSession session) {
        blocks.put(block, session);
    }

    public void removeBlock(Block block, GameSession session) {
        blocks.remove(block, session);
    }

    public void removeBlocksFromSession(GameSession session) {
        for (Map.Entry<Block, GameSession> entry : blocks.entrySet()) {
            if (entry.getValue().equals(session)) {
                blocks.remove(entry.getKey());
            }
        }
    }

    public static BlockManager getInstance() {
        if (instance == null) instance = new BlockManager();
        return instance;
    }
}
