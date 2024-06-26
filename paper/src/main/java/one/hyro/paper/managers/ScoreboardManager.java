package one.hyro.paper.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.enums.PlayerRanks;
import one.hyro.paper.HyroPaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {
    private final Scoreboard scoreboard;
    private final FileConfiguration config;

    public ScoreboardManager() {
        this.config = HyroPaper.getInstance().getConfig();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    private void setCustomMainTablist(Player player) {
        boolean enabled = config.getBoolean("scoreboard.tablist");
        if (!enabled) return;
        player.sendPlayerListHeaderAndFooter(
                Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.BLUE),
                Component.text("ᴘʟᴀɴᴇᴛ.ʜʏʀᴏ.ᴏɴᴇ", NamedTextColor.GOLD)
        );
    }

    private void setDisplayNameTags(Player player) {
        scoreboard.getTeam(PlayerRanks.OWNER.name()).addPlayer(player);

        Component component = PlayerRanks.OWNER.getPrefix()
                .append(Component.text(player.getName(), NamedTextColor.GRAY));

        player.displayName(component);
    }

    public void registerRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) != null) continue;
            Team team = scoreboard.registerNewTeam(rank.name());
            if (rank.getPrefix() == null) continue;
            team.prefix(rank.getPrefix());
            team.color(NamedTextColor.GRAY);
        }
    }

    public void unregisterRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) == null) continue;
            scoreboard.getTeam(rank.name()).unregister();
        }
    }

    public void updateScoreboard(Player player) {
        BukkitScheduler scheduler = HyroPaper.getInstance().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(HyroPaper.getInstance(), () -> {
            setCustomMainTablist(player);
            setDisplayNameTags(player);
            player.setScoreboard(scoreboard);
        }, 0L, 20L);
    }
}