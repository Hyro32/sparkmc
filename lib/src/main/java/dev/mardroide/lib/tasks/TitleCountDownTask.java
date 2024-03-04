package dev.mardroide.lib.tasks;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleCountDownTask {
    private JavaPlugin plugin;
    private Player player;
    private int seconds;
    private ChatColor color;

    public TitleCountDownTask(JavaPlugin plugin, Player player, int seconds, ChatColor color) {
        this.plugin = plugin;
        this.player = player;
        this.seconds = seconds;
        this.color = color;
        start();
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + seconds + "\",color:" + color.name().toLowerCase() + "}");
                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                if (seconds > 0) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
                    seconds--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);

    }
}
