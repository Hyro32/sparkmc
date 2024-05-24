package one.hyro.bukkit.managers;

import one.hyro.bukkit.HyroBukkit;
import one.hyro.lib.utils.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class ScoreboardManager {
    public static Scoreboard setScoreboard() {
        boolean enabled = HyroBukkit.getInstance().getConfig().getBoolean("scoreboard.enabled");
        if (!enabled) return null;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("hyro", "dummy");

        String title = HyroBukkit.getInstance().getConfig().getString("scoreboard.title");
        objective.setDisplayName(Formatter.colorize(title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> body = HyroBukkit.getInstance().getConfig().getStringList("scoreboard.body");

        for (String line : body) {
            Score score = objective.getScore(Formatter.colorize(line));
            score.setScore(body.indexOf(line));
        }

        return scoreboard;
    }
}
