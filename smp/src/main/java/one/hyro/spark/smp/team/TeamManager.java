package one.hyro.spark.smp.team;

import one.hyro.spark.smp.SparkSmp;
import one.hyro.spark.smp.chat.ChatFilter;
import one.hyro.spark.smp.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class TeamManager implements CommandExecutor {

    private final File dbFile;
    private final String prefix;
    private final SparkSmp plugin; //
    private final File messageIdFile;

    public TeamManager(SparkSmp plugin) {
        this.dbFile = new File(plugin.getDataFolder(), "teams.db");
        this.prefix = ChatUtils.getPrefix();
        this.plugin = plugin;
        this.messageIdFile = new File(plugin.getDataFolder(), "lastMessageId.txt");
        initializeDatabase();
    }

    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS teams (" +
                    "team_name TEXT PRIMARY KEY," +
                    "owner TEXT NOT NULL," +
                    "creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "is_open BOOLEAN NOT NULL DEFAULT TRUE," +
                    "home_world TEXT," +
                    "home_x REAL," +
                    "home_y REAL," +
                    "home_z REAL," +
                    "home_yaw REAL," +
                    "home_pitch REAL);");
            statement.execute("CREATE TABLE IF NOT EXISTS team_members (" +
                    "team_name TEXT NOT NULL," +
                    "player TEXT NOT NULL," +
                    "is_invited BOOLEAN NOT NULL DEFAULT FALSE," +
                    "PRIMARY KEY (team_name, player)," +
                    "FOREIGN KEY (team_name) REFERENCES teams (team_name));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(prefix + ChatColor.RED + "Usage: /team <subcommand> [args]");
            return true;
        }

        String subcommand = args[0];

        switch (subcommand.toLowerCase()) {
            case "rename":
                if (args.length != 2) {
                    player.sendMessage(prefix + ChatColor.RED + "Usage: /team rename <new_team_name>");
                    return true;
                }
                renameTeam(player, args[1]);
                break;
            case "create":
                if (args.length != 2) {
                    player.sendMessage(prefix + ChatColor.RED + "Usage: /team create <team_name>");
                    return true;
                }
                createTeam(player, args[1]);
                break;

            case "disband":
                if (args.length != 2) {
                    player.sendMessage(prefix + ChatColor.RED + "Usage: /team disband <team_name>");
                    return true;
                }
                disbandTeam(player, args[1]);
                break;

            case "invite":
                if (args.length != 2) {
                    player.sendMessage(prefix + ChatColor.RED + "Usage: /team invite <player_name>");
                    return true;
                }
                inviteToTeam(player, args[1]);
                break;

            case "join":
                if (args.length != 2) {
                    player.sendMessage(prefix + ChatColor.RED + "Usage: /team join <team_name>");
                    return true;
                }
                joinTeam(player, args[1]);
                break;

            case "leave":
                leaveTeam(player);
                break;

            case "open":
                openTeam(player);
                break;

            case "close":
                closeTeam(player);
                break;

            case "sethome":
                setTeamHome(player);
                break;

            case "home":
                teleportToTeamHome(player);
                break;

            case "delhome":
                deleteTeamHome(player);
                break;

            default:
                player.sendMessage(prefix + ChatColor.RED + "Unknown subcommand. Use /team <create|disband|invite|join|leave|open|close|sethome|home|delhome>");
                break;
        }

        return true;
    }

    private void renameTeam(Player player, String newTeamName) {
        String oldTeamName = getTeamName(player);

        if (oldTeamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        if (playerAlreadyHasTeam(player)) {
            if (teamExists(newTeamName)) {
                player.sendMessage(prefix + ChatColor.RED + "A team with the name '" + ChatColor.AQUA + newTeamName + ChatColor.RED + "' already exists.");
                return;
            }

            if (oldTeamName.equals(newTeamName)) {
                player.sendMessage(prefix + ChatColor.RED + "The new team name is the same as the current team name.");
                return;
            }

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
                // Begin transaction
                connection.setAutoCommit(false);

                // Update team name in teams table
                PreparedStatement updateTeamStmt = connection.prepareStatement("UPDATE teams SET team_name = ? WHERE team_name = ?");
                updateTeamStmt.setString(1, newTeamName);
                updateTeamStmt.setString(2, oldTeamName);
                int rowsAffected = updateTeamStmt.executeUpdate();

                // Update team name in team_members table
                PreparedStatement updateMembersStmt = connection.prepareStatement("UPDATE team_members SET team_name = ? WHERE team_name = ?");
                updateMembersStmt.setString(1, newTeamName);
                updateMembersStmt.setString(2, oldTeamName);
                updateMembersStmt.executeUpdate();

                // Commit transaction
                connection.commit();
                connection.setAutoCommit(true);

                if (rowsAffected > 0) {
                    player.sendMessage(prefix + ChatColor.GREEN + "Team renamed to " + ChatColor.BOLD + ChatColor.AQUA + newTeamName + ChatColor.RESET + ChatColor.GREEN + " successfully!");
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Failed to rename the team. Please try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage(prefix + ChatColor.RED + "An error occurred while renaming the team.");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + "You do not own a team.");
        }
    }


    private void setTeamHome(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        if (!isTeamOwner(player, teamName)) {
            player.sendMessage(prefix + ChatColor.RED + "Only the team owner can set the team home.");
            return;
        }

        Location location = player.getLocation();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE teams SET home_world = ?, home_x = ?, home_y = ?, home_z = ?, home_yaw = ?, home_pitch = ? WHERE team_name = ?");
            stmt.setString(1, location.getWorld().getName());
            stmt.setDouble(2, location.getX());
            stmt.setDouble(3, location.getY());
            stmt.setDouble(4, location.getZ());
            stmt.setFloat(5, location.getYaw());
            stmt.setFloat(6, location.getPitch());
            stmt.setString(7, teamName);
            stmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.GREEN + "Team home set successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void teleportToTeamHome(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        Location homeLocation = getTeamHomeLocation(teamName);
        if (homeLocation == null) {
            player.sendMessage(prefix + ChatColor.RED + "Your team does not have a home set.");
            return;
        }

        // Store the player's initial location and UUID
        Location initialLocation = player.getLocation().clone();
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            private int countdown = 5;

            @Override
            public void run() {
                if (!player.isOnline() || player.getUniqueId() != playerId) {
                    cancel();
                    return;
                }

                // Check if the player has moved
                if (!player.getLocation().equals(initialLocation)) {
                    player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    cancel();
                    return;
                }

                // Countdown logic
                if (countdown > 0) {
                    String actionBar = ChatColor.RED + "Teleporting to team home in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    countdown--;
                } else {
                    // Perform teleportation if player has not moved
                    if (player.getLocation().equals(initialLocation)) {
                        player.teleport(homeLocation);
                        String actionBar = ChatColor.GREEN + "Teleported to team home!";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second (20 ticks)
    }


    private void deleteTeamHome(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        if (!isTeamOwner(player, teamName)) {
            player.sendMessage(prefix + ChatColor.RED + "Only the team owner can delete the team home.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE teams SET home_world = NULL, home_x = NULL, home_y = NULL, home_z = NULL, home_yaw = NULL, home_pitch = NULL WHERE team_name = ?");
            stmt.setString(1, teamName);
            stmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.GREEN + "Team home deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Location getTeamHomeLocation(String teamName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT home_world, home_x, home_y, home_z, home_yaw, home_pitch FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String worldName = rs.getString("home_world");
                double x = rs.getDouble("home_x");
                double y = rs.getDouble("home_y");
                double z = rs.getDouble("home_z");
                float yaw = rs.getFloat("home_yaw");
                float pitch = rs.getFloat("home_pitch");

                if (worldName != null) {
                    return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createTeam(Player player, String teamName) {
        if (playerAlreadyHasTeam(player)) {
            player.sendMessage(prefix + ChatColor.RED + "You already own a team. You cannot create more than one team.");
            return;
        }

        // Check for profane words in the team name
        ArrayList<String> badWords = ChatFilter.badWordsFound(teamName);
        if (!badWords.isEmpty()) {
            player.sendMessage(prefix + ChatColor.RED + "The team name '" + ChatColor.AQUA + teamName + ChatColor.RED + "' contains inappropriate language.");
            return;
        }

        if (teamExists(teamName)) {
            player.sendMessage(prefix + ChatColor.RED + "A team with the name '" + ChatColor.AQUA + teamName + ChatColor.RED + "' already exists.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO teams (team_name, owner) VALUES (?, ?)");
            stmt.setString(1, teamName);
            stmt.setString(2, player.getName());
            stmt.executeUpdate();

            PreparedStatement memberStmt = connection.prepareStatement("INSERT INTO team_members (team_name, player, is_invited) VALUES (?, ?, ?)");
            memberStmt.setString(1, teamName);
            memberStmt.setString(2, player.getName());
            memberStmt.setBoolean(3, false);
            memberStmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.GREEN + " created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void disbandTeam(Player player, String teamName) {
        if (!isTeamOwner(player, teamName)) {
            player.sendMessage(prefix + ChatColor.RED + "You are not the owner of the team " + ChatColor.AQUA + teamName + ChatColor.RED + ".");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            stmt.executeUpdate();

            PreparedStatement memberStmt = connection.prepareStatement("DELETE FROM team_members WHERE team_name = ?");
            memberStmt.setString(1, teamName);
            memberStmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.RED + "Team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.RED + " has been disbanded.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void inviteToTeam(Player inviter, String playerName) {
        if (!teamExistsForPlayer(inviter)) {
            inviter.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            inviter.sendMessage(prefix + ChatColor.RED + "Player " + ChatColor.AQUA + playerName + ChatColor.RED + " is not online.");
            return;
        }

        String teamName = getTeamName(inviter);
        if (isPlayerInAnyTeam(targetPlayer)) {
            String currentTeamName = getTeamName(targetPlayer);
            if (currentTeamName != null) {
                leaveTeam(targetPlayer, currentTeamName); // Call the overload with teamName
            }
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("INSERT OR REPLACE INTO team_members (team_name, player, is_invited) VALUES (?, ?, ?)");
            stmt.setString(1, teamName);
            stmt.setString(2, playerName);
            stmt.setBoolean(3, true);
            stmt.executeUpdate();

            inviter.sendMessage(prefix + ChatColor.GREEN + "Player " + ChatColor.AQUA + playerName + ChatColor.GREEN + " has been invited to team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.GREEN + ".");
            targetPlayer.sendMessage(prefix + ChatColor.YELLOW + "You have been invited to join team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.YELLOW + ". Use /team join " + ChatColor.AQUA + teamName + ChatColor.YELLOW + " to join.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void joinTeam(Player player, String teamName) {
        if (playerAlreadyHasTeam(player)) {
            player.sendMessage(prefix + ChatColor.RED + "You are already in a team.");
            return;
        }

        if (!teamExists(teamName)) {
            player.sendMessage(prefix + ChatColor.RED + "No such team exists.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT is_invited FROM team_members WHERE team_name = ? AND player = ?");
            stmt.setString(1, teamName);
            stmt.setString(2, player.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("is_invited")) {
                    PreparedStatement updateStmt = connection.prepareStatement("UPDATE team_members SET is_invited = FALSE WHERE team_name = ? AND player = ?");
                    updateStmt.setString(1, teamName);
                    updateStmt.setString(2, player.getName());
                    updateStmt.executeUpdate();

                    player.sendMessage(prefix + ChatColor.GREEN + "You have successfully joined the team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.GREEN + ".");
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "You were not invited to this team.");
                }
            } else {
                player.sendMessage(prefix + ChatColor.RED + "No such team invitation found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void leaveTeam(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        leaveTeam(player, teamName); // Call the overload method
    }

    private void leaveTeam(Player player, String teamName) {
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "Invalid team name.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM team_members WHERE team_name = ? AND player = ?");
            stmt.setString(1, teamName);
            stmt.setString(2, player.getName());
            stmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.RED + "You have left the team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.RED + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openTeam(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE teams SET is_open = TRUE WHERE team_name = ?");
            stmt.setString(1, teamName);
            stmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.GREEN + " is now open for anyone to join.");

            // Notify Discord
            plugin.notifyDiscordChannel(dbFile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeTeam(Player player) {
        String teamName = getTeamName(player);
        if (teamName == null) {
            player.sendMessage(prefix + ChatColor.RED + "You are not in any team.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE teams SET is_open = FALSE WHERE team_name = ?");
            stmt.setString(1, teamName);
            stmt.executeUpdate();

            player.sendMessage(prefix + ChatColor.RED + "Team " + ChatColor.BOLD + ChatColor.AQUA + teamName + ChatColor.RESET + ChatColor.RED + " is now closed.");

            // Notify Discord
            plugin.notifyDiscordChannel(dbFile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean teamExists(String teamName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean teamExistsForPlayer(Player player) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM team_members WHERE player = ?");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean playerAlreadyHasTeam(Player player) {
        return getTeamName(player) != null;
    }

    private boolean isTeamOwner(Player player, String teamName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT owner FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("owner").equals(player.getName());
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPlayerInAnyTeam(Player player) {
        return teamExistsForPlayer(player);
    }

    private String getTeamName(Player player) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT team_name FROM team_members WHERE player = ? AND is_invited = FALSE");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("team_name");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
