package one.hyro.duels.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import one.hyro.builders.GameMenu;
import one.hyro.enums.GameStatus;
import one.hyro.managers.GameManager;
import one.hyro.managers.MenuManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteKitCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;
        MenuManager menuManager = MenuManager.getInstance();
        GameManager gameManager = new GameManager();

        if (gameManager.isInGame(player) && gameManager.getGameSession(player).getStatus() == GameStatus.STARTING) {
            GameMenu voteMenu = menuManager.getMenu("vote-kit");
            player.openInventory(voteMenu.getInventory());
        }
    }
}
