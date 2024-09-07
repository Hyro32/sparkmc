package one.hyro.spark.smp.team;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TeamTabCompleter implements TabCompleter {

    private final String dbPath;

    public TeamTabCompleter(File dbFile) {
        this.dbPath = "jdbc:sqlite:" + dbFile.getAbsolutePath();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return completions;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            // First argument: show subcommands (create, disband, join, invite, leave, open, close, sethome, home, delhome)
            completions.add("create");
            completions.add("disband");
            completions.add("join");
            completions.add("invite");
            completions.add("leave");
            completions.add("open");
            completions.add("close");
            completions.add("sethome");
            completions.add("home");
            completions.add("delhome");
            completions.add("rename");
        } else if (args.length == 2) {
            // Second argument
            switch (args[0].toLowerCase()) {
                case "disband":
                case "close":
                case "open":
                case "sethome":
                case "home":
                case "delhome":
                    // Show team names that the player owns or is in
                    if (playerHasTeam(player)) {
                        String playerTeam = getPlayerTeam(player);
                        completions.add(playerTeam);
                    }
                    break;

                case "join":
                    // Show open team names
                    completions.addAll(getAllOpenTeamNames());
                    break;

                case "invite":
                    // Show online players (for inviting)
                    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                        if (!onlinePlayer.equals(player) && !isPlayerInTeam(onlinePlayer)) {
                            completions.add(onlinePlayer.getName());
                        }
                    }
                    break;

                case "leave":
                    // Show current team name if player is in a team
                    if (playerHasTeam(player)) {
                        completions.add(getPlayerTeam(player));
                    }
                    break;
            }
        }
        return completions;
    }

    private boolean playerHasTeam(Player player) {
        try (Connection connection = DriverManager.getConnection(dbPath)) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM team_members WHERE player = ?");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getPlayerTeam(Player player) {
        try (Connection connection = DriverManager.getConnection(dbPath)) {
            PreparedStatement stmt = connection.prepareStatement("SELECT team_name FROM team_members WHERE player = ?");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("team_name") : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getAllOpenTeamNames() {
        List<String> teamNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbPath)) {
            PreparedStatement stmt = connection.prepareStatement("SELECT team_name FROM teams WHERE is_open = 1");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teamNames.add(rs.getString("team_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamNames;
    }

    private boolean isPlayerInTeam(Player player) {
        try (Connection connection = DriverManager.getConnection(dbPath)) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM team_members WHERE player = ?");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
