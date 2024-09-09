package one.hyro.spark.lobby.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.lib.builder.SparkItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player joiner = event.getPlayer();

        SparkItem compass = new SparkItem(Material.COMPASS)
                .setDisplayName(Component.text("Minigames", NamedTextColor.BLUE))
                .setLore(Component.text("Click to open the minigames selector!", NamedTextColor.GRAY))
                .onInteract(player -> player.performCommand("minigames"))
                .build();

        SparkItem star = new SparkItem(Material.NETHER_STAR)
                .setDisplayName(Component.text("Lobby Selector", NamedTextColor.LIGHT_PURPLE))
                .setLore(Component.text("Click to open the lobbies menu!", NamedTextColor.GRAY))
                .onInteract(player -> player.performCommand("lobbies"))
                .build();

        joiner.getInventory().clear();
        joiner.getInventory().setItem(0, compass.getStack());
        joiner.getInventory().setItem(8, star.getStack());
    }
}
