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

public class LeaveCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;

        QueueManager queueManager = new QueueManager();
        GameManager gameManager = new GameManager();

        if (queueManager.isPlayerInQueue(player)) {
            queueManager.removePlayerFromQueue(player);
            return;
        }

        if (gameManager.isInGame(player)) {
            GameSession session = gameManager.getGameSession(player);
            session.removePlayer(player);
            session.setGameStatus(GameStatus.ENDING);
            return;
        }

        player.sendMessage(Component.text("You are not in a queue or game!", NamedTextColor.RED));
    }
}
