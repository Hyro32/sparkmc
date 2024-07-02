package one.hyro.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.data.PlayerData;
import org.bukkit.entity.Player;

public class TagManager {
    public static void setRankDisplayName(Player player) {
        Component component = PlayerData.getPlayerData(player.getUniqueId()).getRank().getPrefix()
                .append(Component.text(player.getName(), NamedTextColor.GRAY));
        player.displayName(component);
    }
}
