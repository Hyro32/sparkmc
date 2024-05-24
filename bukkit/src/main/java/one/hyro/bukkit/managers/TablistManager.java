package one.hyro.bukkit.managers;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import one.hyro.bukkit.HyroBukkit;
import one.hyro.lib.utils.Formatter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class TablistManager {
    public static void setTablist(Player player) {
        boolean enabled = HyroBukkit.getInstance().getConfig().getBoolean("tablist.enabled");
        if (!enabled) return;

        List<String> headers = HyroBukkit.getInstance().getConfig().getStringList("tablist.header");
        List<String> footers = HyroBukkit.getInstance().getConfig().getStringList("tablist.footer");

        if (headers.isEmpty() && footers.isEmpty()) return;

        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Formatter.colorize(String.join("\n", headers)) + "\"}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Formatter.colorize(String.join("\n", footers)) + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(header);

        try {
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, footer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
