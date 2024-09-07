package one.hyro.spark.smp.scoreboard;

import com.tserato.smp.SMP;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomScoreboard {

    private final JavaPlugin plugin;

    public CustomScoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
        if (!(plugin instanceof SMP)) {
            throw new IllegalStateException("Plugin must be an instance of SMP");
        }
    }

    public void createScoreboard(Player player) {
        // Get the scoreboard manager
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        // Create an objective in the sidebar with the title
        String title = ChatColor.RED + "" + ChatColor.BOLD + "❤ " +
                ChatColor.GRAY + "" + ChatColor.BOLD + "CoupSquad " +
                ChatColor.RED + "" + ChatColor.BOLD + "❤";
        Objective objective = board.registerNewObjective("CoupSquad", "dummy", title);
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

        // Set up scores
        updateScoreboard(player, board, objective);

        // Set the scoreboard to the player
        player.setScoreboard(board);

        // Update the scoreboard every 5 ticks (0.25 seconds)
        new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboard(player, board, objective);
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    private void updateScoreboard(Player player, Scoreboard board, Objective objective) {
        // Clear existing scores
        board.getEntries().forEach(board::resetScores);

        // Recreate scores with updated values
        Score titleBlank = objective.getScore(" ");
        titleBlank.setScore(9);

        Score playerName = objective.getScore(ChatColor.GREEN + "Name: " + ChatColor.GRAY + player.getName());
        playerName.setScore(8);

        Score blank1 = objective.getScore("  ");
        blank1.setScore(7);

        // Get the player's balance using PlaceholderAPI
        String moneyPlaceholder = "%vault_eco_balance%";
        String money = PlaceholderAPI.setPlaceholders(player, moneyPlaceholder);
        Score playerMoney = objective.getScore(ChatColor.GREEN + "Money: " + ChatColor.GRAY + "$" + money);
        playerMoney.setScore(6);

        Score blank2 = objective.getScore("   ");
        blank2.setScore(5);

        String teamName = getTeamName(player);
        Score playerTeam = objective.getScore(ChatColor.GREEN + "Team: " + ChatColor.GRAY + teamName);
        playerTeam.setScore(4);

        Score blank3 = objective.getScore("    ");
        blank3.setScore(3);

        // Placeholder for playtime; replaced with static text or any calculated value
        String playtimePlaceholder = "%playtime_time%";
        String playtime = PlaceholderAPI.setPlaceholders(player, playtimePlaceholder); // Placeholder value
        Score playerPlaytime = objective.getScore(ChatColor.GREEN + "Playtime: " + ChatColor.GRAY + playtime);
        playerPlaytime.setScore(2);

        Score blank4 = objective.getScore("     ");
        blank4.setScore(1);

        // Get the current date and time
        String dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        String centeredDateTime = centerText(dateTime);
        Score currentDate = objective.getScore(ChatColor.GRAY + centeredDateTime);
        currentDate.setScore(0);
    }

    private String getTeamName(Player player) {
        String teamName = "None"; // Default value if the player is not in a team

        // Connect to the database and fetch the player's team name
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + new File(plugin.getDataFolder(), "teams.db").getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT team_name FROM team_members WHERE player = ? AND is_invited = FALSE");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                teamName = rs.getString("team_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teamName;
    }

    // Utility method to center text by adding spaces before and after
    private String centerText(String text) {
        int maxWidth = 32; // Maximum width of the scoreboard line
        int padding = (maxWidth - text.length()) / 2;
        StringBuilder centeredText = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            centeredText.append(" ");
        }
        centeredText.append(text);
        return centeredText.toString();
    }
}
