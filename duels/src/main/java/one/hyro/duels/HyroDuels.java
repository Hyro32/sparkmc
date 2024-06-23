package one.hyro.duels;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import one.hyro.duels.commands.JoinCommand;
import one.hyro.duels.commands.VoteKitCommand;
import one.hyro.duels.enums.DuelKits;
import one.hyro.duels.events.BlockBreakListener;
import one.hyro.duels.events.BlockPlaceListener;
import one.hyro.duels.events.FoodLevelChangeListener;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;
import one.hyro.managers.BlockManager;
import one.hyro.tasks.GameStartCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyroDuels extends JavaPlugin implements Minigame {
    @Getter
    private static HyroDuels instance;
    private final BlockManager blockManager = new BlockManager();

    @Override
    public void onEnable() {
        instance = this;
        saveResource("config.yml", false);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("join", "Join a duels game", new JoinCommand());
            commands.register("vote", "Vote for a kit", new VoteKitCommand());
        });

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);

        Bukkit.getLogger().info("HyroDuels has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HyroDuels has been disabled!");
    }

    @Override
    public void waiting(GameSession session) {
        session.setGameStatus(GameStatus.STARTING);
    }

    @Override
    public void starting(GameSession session) {
        new GameStartCountdownTask(this, session);
    }

    @Override
    public void inGame(GameSession session) {
        for (Player player : session.getPlayers()) {
            player.setInvulnerable(false);
            player.getInventory().setHelmet(DuelKits.CLASSIC.getHelmet().getItem());
            player.getInventory().setChestplate(DuelKits.CLASSIC.getChestplate().getItem());
            player.getInventory().setLeggings(DuelKits.CLASSIC.getLeggings().getItem());
            player.getInventory().setBoots(DuelKits.CLASSIC.getBoots().getItem());
        }
        session.setGameStatus(GameStatus.ENDING);
    }

    @Override
    public void ending(GameSession session) {
        blockManager.removeBlocksFromSession(session);
    }
}
