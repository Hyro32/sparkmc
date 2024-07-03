package one.hyro.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import one.hyro.builders.CustomItem;
import one.hyro.data.PlayerData;
import one.hyro.instances.GameSession;
import one.hyro.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class Teleport {
    public static void teleportToLobby(Player player, Component leaveMessage) {
        World spawn = Bukkit.getWorld("world");
        if (spawn == null) return;

        player.teleportAsync(spawn.getSpawnLocation()).thenAccept(success -> {
            if (!success) return;
            player.getInventory().clear();
            player.setHealth(20);
            player.setInvulnerable(true);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.ADVENTURE);

            CustomItem compass = new CustomItem(Material.COMPASS)
                    .setCustomId("minigames")
                    .setDisplayName(
                            Component.text("Minigames Selector", NamedTextColor.GOLD)
                                    .decoration(TextDecoration.ITALIC, false)
                    )
                    .onClick(clicker -> clicker.performCommand("minigames"))
                    .build();

            CustomItem profile = new CustomItem(player)
                    .setCustomId("profile")
                    .setDisplayName(
                            Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE)
                                    .decoration(TextDecoration.ITALIC, false)
                    )
                    .build();

            Inventory inventory = player.getInventory();
            inventory.setItem(0, compass.getItem());
            inventory.setItem(1, profile.getItem());

            int level = PlayerData.getPlayerData(player.getUniqueId()).getLevel();
            player.setLevel(level);

            GameManager gameManager = GameManager.getInstance();
            if (gameManager.isPlayerInGame(player.getUniqueId())) {
                GameSession session = gameManager.getGameSession(player.getUniqueId());
                if (session == null) return;

                for (UUID uuid : session.getPlayersUuids()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p == null) continue;
                    p.sendMessage(leaveMessage);
                }
            };
        });
    }

    public static void teleportToGame(Player player, GameSession session, Component joinMessage) {
        World world = session.getMap().getWorld();
        if (world == null) return;

        player.teleportAsync(world.getSpawnLocation()).thenAccept(success -> {
            if (!success) return;
            player.getInventory().clear();

            for (UUID uuid : session.getPlayersUuids()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p == null) continue;
                p.sendMessage(joinMessage);
            }
        });
    }
}
