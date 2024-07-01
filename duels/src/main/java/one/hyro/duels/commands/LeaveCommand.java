package one.hyro.duels.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.duels.managers.QueueManager;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LeaveCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;
        UUID playerUuid = player.getUniqueId();

        QueueManager queueManager = QueueManager.getInstance();
        GameManager gameManager = GameManager.getInstance();

        if (queueManager.isPlayerInQueue(playerUuid)) {
            queueManager.removePlayerFromQueue(playerUuid);
            return;
        }

        if (gameManager.isPlayerInGame(playerUuid)) {
            GameSession session = gameManager.getGameSession(playerUuid);
            session.removePlayer(playerUuid);
            session.setGameStatus(GameStatus.ENDING);
            return;
        }

        player.sendMessage(Component.text("You are not in a queue or game!", NamedTextColor.RED));
    }
}
