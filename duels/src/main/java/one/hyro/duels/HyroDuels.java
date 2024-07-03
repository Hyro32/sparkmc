package one.hyro.duels;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public final class HyroDuels extends JavaPlugin implements Minigame {
    @Getter private static HyroDuels instance;

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
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        TranslationRegistry registry = TranslationRegistry.create(Key.key("duels:i18n"));
        ResourceBundle bundleEN = ResourceBundle.getBundle("translations.en", Locale.ENGLISH, UTF8ResourceBundleControl.get());
        ResourceBundle bundleES = ResourceBundle.getBundle("translations.es", Locale.of("es"), UTF8ResourceBundleControl.get());
        registry.registerAll(Locale.ENGLISH, bundleEN, true);
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true);
        GlobalTranslator.translator().addSource(registry);

        Bukkit.getLogger().info("HyroDuels has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HyroDuels has been disabled!");
    }

    @Override
    public void waiting(GameSession session) {
        if (session.getPlayersUuids().size() < session.getMinPlayers()) return;
        session.setGameStatus(GameStatus.STARTING);
    }

    @Override
    public void starting(GameSession session) {
        int seconds = 10;

        Component countdown = Component.translatable(
                "info.status.countdown",
                Component.text(seconds, NamedTextColor.BLUE)
        ).color(NamedTextColor.GRAY);

        Component start = Component.translatable(
                "info.status.start"
        ).color(NamedTextColor.GREEN);

        new GameStartCountdownTask(this, session, seconds, countdown, start);
    }

    @Override
    public void inGame(GameSession session) {
        DuelGameSession duelSession = (DuelGameSession) session;
        DuelMode mode = duelSession.getMode();

        for (UUID uuid : duelSession.getPlayersUuids()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            mode.setPlayerInventory(player);
            player.setInvulnerable(false);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public void ending(GameSession session) {
        DuelGameSession duelSession = (DuelGameSession) session;

        for (UUID uuid : duelSession.getPlayersUuids()) {
            if (duelSession.getLosers().contains(uuid)) {
                Component title = Component.text("You lost!", NamedTextColor.RED);
                Title titleComponent = Title.title(title, Component.empty());
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                player.showTitle(titleComponent);
            } else {
                Component title = Component.text("You won!", NamedTextColor.GREEN);
                Title titleComponent = Title.title(title, Component.empty());
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                player.getInventory().clear();
                player.showTitle(titleComponent);
            }
        }

        new GameEndCountdownTask(this, session);
    }
}
