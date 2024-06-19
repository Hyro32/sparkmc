package one.hyro.managers;

import lombok.Getter;
import one.hyro.instances.GameSession;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BlockManager {
    private final Map<Block, GameSession> blocks;

    public BlockManager() {
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
}
