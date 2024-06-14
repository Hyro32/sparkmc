package one.hyro.paper.managers;

import net.kyori.adventure.text.Component;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.enums.PlayerRanks;
import one.hyro.paper.utilities.Chalk;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private static final FileConfiguration config = HyrosPaper.getInstance().getConfig();
    private static final String header = config.getString("tablist.header");
    private static final String footer = config.getString("tablist.footer");

    public static void setCustomTablist(Player player) {
        if (header == null || footer == null) return;
        player.sendPlayerListHeaderAndFooter(
                Component.text(Chalk.colorizeLegacy(header)),
                Component.text(Chalk.colorizeLegacy(footer))
        );
        scoreboard.getTeam(PlayerRanks.ADMINISTRATOR.name()).addPlayer(player);
    }

    public static void registerRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) != null) continue;
            Team team = scoreboard.registerNewTeam(rank.name());
            if (rank.getUnicode() == null) continue;
            team.setPrefix(rank.getUnicode() + Chalk.colorizeLegacy(" &8"));
        }
    }
}
