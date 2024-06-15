package one.hyro.paper.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PlayerRanks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagsManager {
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private static final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private static final String header = config.getString("tablist.header");
    private static final String footer = config.getString("tablist.footer");

    public static void setCustomTags(Player player) {
        scoreboard.getTeam(PlayerRanks.DEFAULT.name()).addPlayer(player);

        TextComponent component = Component.text(PlayerRanks.DEFAULT.getUnicode())
                .append(Component.text(" "))
                .append(Component.text(player.getName(), NamedTextColor.GRAY));

        player.displayName(component);

        String headerText = String.join("\n", header);
        String footerText = String.join("\n", footer);

        player.sendPlayerListHeaderAndFooter(
                Component.text(headerText, NamedTextColor.BLUE),
                Component.text(footerText, NamedTextColor.GOLD)
        );
    }

    public static void registerRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) != null) continue;
            Team team = scoreboard.registerNewTeam(rank.name());
            if (rank.getUnicode() == null) continue;
            TextComponent prefix = Component.text(rank.getUnicode() + " ");
            team.prefix(prefix);
        }
    }

    public static void unregisterRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) == null) continue;
            scoreboard.getTeam(rank.name()).unregister();
        }
    }
}
