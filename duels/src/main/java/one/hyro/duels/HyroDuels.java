package one.hyro.duels;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import one.hyro.duels.commands.JoinCommand;
import one.hyro.duels.commands.LeaveCommand;
import one.hyro.duels.enums.DuelMode;
import one.hyro.duels.events.*;
import one.hyro.duels.instances.DuelGameSession;
import one.hyro.enums.GameStatus;
import one.hyro.instances.GameSession;
import one.hyro.instances.Minigame;
import one.hyro.tasks.GameEndCountdownTask;
import one.hyro.tasks.GameStartCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyroDuels extends JavaPlugin implements Minigame {
    @Getter
    private static HyroDuels instance;

    @Override
    public void onEnable() {
        instance = this;
        saveResource("config.yml", false);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("join", "Join a duels game", new JoinCommand());
            commands.register("leave", "Leave a duels game or queue", new LeaveCommand());
        });

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);

        Bukkit.getLogger().info("HyroDuels has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HyroDuels has been disabled!");
    }

    @Override
    public void waiting(GameSession session) {
        for (Player player : session.getPlayers()) player.getInventory().clear();
        session.setGameStatus(GameStatus.STARTING);
    }

    @Override
    public void starting(GameSession session) {
        new GameStartCountdownTask(this, session);
    }

    @Override
    public void inGame(GameSession session) {
        DuelGameSession duelSession = (DuelGameSession) session;
        DuelMode mode = duelSession.getMode();

        for (Player player : duelSession.getPlayers()) {
            mode.setPlayerInventory(player);
            player.setInvulnerable(false);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public void ending(GameSession session) {
        new GameEndCountdownTask(this, session);
    }
}
