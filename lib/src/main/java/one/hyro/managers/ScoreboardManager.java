package one.hyro.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.data.PlayerData;
import one.hyro.enums.MinigameTeams;
import one.hyro.enums.PlayerRanks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

public class ScoreboardManager {
    private Plugin plugin;
    private Scoreboard scoreboard;

    public ScoreboardManager(Plugin plugin) {
        this.plugin = plugin;
        this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
    }

    private void setPlayerRankTeam(Player player) {
        PlayerRanks rank = PlayerData.getPlayerData(player.getUniqueId()).getRank();
        Team team = scoreboard.getTeam(rank.name());
        if (team == null) return;
        team.addEntry(player.getName());
    }

    public void setCustomMainTab(Player player) {
        player.sendPlayerListHeaderAndFooter(
                Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.BLUE),
                Component.text("ᴘʟᴀɴᴇᴛ.ʜʏʀᴏ.ᴏɴᴇ", NamedTextColor.GOLD)
        );
        setPlayerRankTeam(player);
    }

    public void setCustomTab(Component header, Component footer, Player player) {
        player.sendPlayerListHeaderAndFooter(header, footer);
        setPlayerRankTeam(player);
    }

    public void setMinigameTeams(List<UUID> uuids, int teamCount, int teamSize) {
        registerTeams();
        for (UUID uuid : uuids) {
            for (int i = 0; i < teamCount; i++) {
                Team team = scoreboard.getTeam(MinigameTeams.values()[i].getName());
                if (team == null) continue;
                if (team.getSize() < teamSize) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    team.addPlayer(player);
                    break;
                }
            }
        }
    }

    public void removePlayerFromMinigameTeam(UUID uuid) {
        for (MinigameTeams team : MinigameTeams.values()) {
            Team scoreboardTeam = scoreboard.getTeam(team.getName());
            if (scoreboardTeam == null) continue;
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            scoreboardTeam.removePlayer(player);
        }
    }

    public void registerTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) != null) continue;
            Team team = scoreboard.registerNewTeam(rank.getWeight() + rank.name());
            if (rank.getPrefix() == null) continue;
            team.prefix(rank.getPrefix());
            team.color(NamedTextColor.GRAY);
        }

        for (MinigameTeams minigameTeam : MinigameTeams.values()) {
            if (scoreboard.getTeam(minigameTeam.getName()) != null) continue;
            Team team = scoreboard.registerNewTeam(minigameTeam.getName());
            team.color(minigameTeam.getColor());
        }
    }

    public void unregisterTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            Team team = scoreboard.getTeam(rank.name());
            if (team == null) continue;
            team.unregister();
        }

        for (MinigameTeams minigameTeam : MinigameTeams.values()) {
            Team team = scoreboard.getTeam(minigameTeam.getName());
            if (team == null) continue;
            team.unregister();
        }
    }

    public void updateScoreboard() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                setCustomMainTab(player);
                player.setScoreboard(scoreboard);
            }
        }, 0L, 20L);
    }
}