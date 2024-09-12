package one.hyro.spark.lobby.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.builder.SparkMenu;
import one.hyro.spark.lobby.SparkLobby;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player joiner = event.getPlayer();

        ByteArrayDataOutput smpout = ByteStreams.newDataOutput();
        smpout.writeUTF("Connect");
        smpout.writeUTF("smp");

        ByteArrayDataOutput duelsout = ByteStreams.newDataOutput();
        duelsout.writeUTF("Connect");
        duelsout.writeUTF("minigames");

        ByteArrayDataOutput pvpout = ByteStreams.newDataOutput();
        pvpout.writeUTF("Connect");
        pvpout.writeUTF("pvp");


        SparkItem minigames = new SparkItem(Material.DIAMOND_CHESTPLATE)
                .setDisplayName(Component.text("Duels", NamedTextColor.AQUA))
                .setLore(Component.text("Play 1vs1 or 2vs2 duels!", NamedTextColor.YELLOW))
                .onInteract(player -> player.sendPluginMessage(SparkLobby.getInstance(), "BungeeCord", duelsout.toByteArray()))
                .build();

        SparkItem smp = new SparkItem(Material.GRASS_BLOCK)
                .setDisplayName(Component.text("SMP", NamedTextColor.RED))
                .setLore(Component.text("Click to join the SMP world!", NamedTextColor.GREEN))
                .onInteract(player -> player.sendPluginMessage(SparkLobby.getInstance(), "BungeeCord", smpout.toByteArray()))
                .build();

        SparkItem pvp = new SparkItem(Material.DIAMOND_SWORD)
                .setDisplayName(Component.text("PvP Arena", NamedTextColor.GOLD))
                .setLore(Component.text("Click to join the PvP Arena World!", NamedTextColor.GREEN))
                .onInteract(player -> player.sendPluginMessage(SparkLobby.getInstance(), "BungeeCord", pvpout.toByteArray()))
                .build();

        SparkMenu servermenu = new SparkMenu()
                .setRows(3)
                .setTitle(Component.text("Server Menu", NamedTextColor.YELLOW))
                .setItem(11, pvp)
                .setItem(13, smp)
                .setItem(15, minigames)
                .build();

        SparkItem star = new SparkItem(Material.NETHER_STAR)
                .setDisplayName(Component.text("Server Selector", NamedTextColor.LIGHT_PURPLE))
                .setLore(Component.text("Click to open the lobbies menu!", NamedTextColor.GRAY))
                .onInteract(player -> player.openInventory(servermenu.getInventory()))
                .build();

        joiner.getInventory().setItem(4, star.getStack());
    }
}
