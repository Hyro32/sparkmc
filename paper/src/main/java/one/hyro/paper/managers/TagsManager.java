package one.hyro.paper.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.enums.PlayerRanks;
import one.hyro.paper.HyrosPaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

public class TagsManager {
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private static final FileConfiguration config = HyrosPaper.getInstance().getConfig();

    private static void setCustomTablist(Player player) {
        boolean enabled = config.getBoolean("scoreboard.tablist");
        if (!enabled) return;
        player.sendPlayerListHeaderAndFooter(
                Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.BLUE),
                Component.text("ᴘʟᴀɴᴇᴛ.ʜʏʀᴏ.ᴏɴᴇ", NamedTextColor.GOLD)
        );
    }

    private static void setDisplayNameTags(Player player) {
        scoreboard.getTeam(PlayerRanks.OWNER.name()).addPlayer(player);

        TextComponent component = Component.text(PlayerRanks.OWNER.getPrefix())
                .append(Component.text(" "))
                .append(Component.text(player.getName(), NamedTextColor.GRAY));

        player.displayName(component);
    }

    public static void registerRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) != null) continue;
            Team team = scoreboard.registerNewTeam(rank.name());
            if (rank.getPrefix() == null) continue;
            TextComponent prefix = Component.text(rank.getPrefix() + " ");
            team.prefix(prefix);
        }
    }

    public static void unregisterRanksTeams() {
        for (PlayerRanks rank : PlayerRanks.values()) {
            if (scoreboard.getTeam(rank.name()) == null) continue;
            scoreboard.getTeam(rank.name()).unregister();
        }
    }

    public static void updateScoreboard(Player player) {
        BukkitScheduler scheduler = HyrosPaper.getInstance().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(HyrosPaper.getInstance(), () -> {
            setCustomTablist(player);
            setDisplayNameTags(player);
            player.setScoreboard(scoreboard);
        }, 0L, 20L);
    }
}