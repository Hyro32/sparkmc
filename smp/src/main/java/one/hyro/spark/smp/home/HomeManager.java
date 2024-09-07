package one.hyro.spark.smp.home;

import com.tserato.smp.SMP;
import com.tserato.smp.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager implements CommandExecutor {

    private final File dbFile;
    private final String prefix;
    private final HomeGUIManager homeGUIManager;

    public HomeManager(File dbFile) {
        this.dbFile = dbFile;
        this.prefix = ChatUtils.getPrefix();
        this.homeGUIManager = new HomeGUIManager(this, JavaPlugin.getPlugin(SMP.class)); // Pass plugin instance
        initializeDatabase();
    }

    public File getDbFile() {
        return dbFile;
    }

    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            Statement statement = connection.createStatement();
            // Create the table without the playtime column
            statement.execute("CREATE TABLE IF NOT EXISTS homes (" +
                    "player TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "world TEXT NOT NULL," +
                    "x REAL NOT NULL," +
                    "y REAL NOT NULL," +
                    "z REAL NOT NULL," +
                    "yaw REAL NOT NULL," +
                    "pitch REAL NOT NULL," +
                    "creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (player, name));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getMaxHomes(Player player) {
        // Iterate over possible permissions to find the maximum homes permission
        for (int i = 100; i > 0; i--) {  // Assuming 100 as a reasonable upper limit for max homes
            if (player.hasPermission("smp.maxhomes." + i)) {
                return i;
            }
        }
        // Default to 2 homes if no specific permission is found
        return 2;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("sethome")) {
            if (args.length == 0) {
                args = new String[]{"home"};  // Default home name
            }

            String homeName = args[0];

            // Check if the home name already exists
            if (homeExists(player, homeName)) {
                player.sendMessage(prefix + ChatColor.RED + "A home with the name '" + ChatColor.AQUA + homeName + ChatColor.RED + "' already exists.");
                return true;
            }

            setHome(player, homeName);
            return true;

        } else if (command.getName().equalsIgnoreCase("home")) {
            if (args.length == 0) {
                homeGUIManager.openHomeGUI(player);
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /home <name>");
                return true;
            }

            String homeName = args[0];
            teleportToHome(player, homeName);
            return true;

        } else if (command.getName().equalsIgnoreCase("homes")) {
            if (args.length == 0) {
                homeGUIManager.openHomeGUI(player);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("delhome")) {
            if (args.length != 1) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /delhome <name>");
                return true;
            }

            String homeName = args[0];

            // Check if the home name exists before deleting
            if (!homeExists(player, homeName)) {
                player.sendMessage(prefix + ChatColor.RED + "Home " + ChatColor.AQUA + homeName + ChatColor.RED + " does not exist.");
                return true;
            }

            deleteHome(player, homeName);
            player.sendMessage(prefix + ChatColor.WHITE + "Deleted Home " + ChatColor.BOLD + ChatColor.AQUA + homeName + ChatColor.WHITE + ".");
            return true;
        }

        return false;
    }

    private void setHome(Player player, String homeName) {
        List<String> homeNames = getHomeNames(player);
        int maxHomes = getMaxHomes(player);  // Using the new getMaxHomes method

        if (homeNames.size() >= maxHomes) {
            player.sendMessage(prefix + ChatColor.RED + "You have reached the maximum number of homes (" + ChatColor.AQUA + maxHomes + ChatColor.RED + ").");
            return;
        }

        player.sendMessage(prefix + "Created Home " + ChatColor.BOLD + ChatColor.AQUA + homeName + ChatColor.RESET + ".");

        Location loc = player.getLocation();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "REPLACE INTO homes (player, name, world, x, y, z, yaw, pitch, creation_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            stmt.setString(3, loc.getWorld().getName());
            stmt.setDouble(4, loc.getX());
            stmt.setDouble(5, loc.getY());
            stmt.setDouble(6, loc.getZ());
            stmt.setFloat(7, loc.getYaw());
            stmt.setFloat(8, loc.getPitch());
            stmt.setString(9, new Timestamp(System.currentTimeMillis()).toString()); // Save the current timestamp
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean homeExists(Player player, String homeName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void teleportToHome(Player player, String homeName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Location loc = new Location(
                        Bukkit.getWorld(rs.getString("world")),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z"),
                        rs.getFloat("yaw"),
                        rs.getFloat("pitch")
                );

                // Store the player's initial location and UUID
                Location initialLocation = player.getLocation().clone();
                UUID playerId = player.getUniqueId();

                new BukkitRunnable() {
                    private int countdown = 3;

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
                            String actionBar = ChatColor.RED + "Teleporting in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                            countdown--;
                        } else {
                            // Perform teleportation if player has not moved
                            if (player.getLocation().equals(initialLocation)) {
                                player.teleport(loc);
                                String actionBar = ChatColor.GREEN + "Teleported!";
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                            }
                            cancel();
                        }
                    }
                }.runTaskTimer(JavaPlugin.getPlugin(SMP.class), 0L, 20L); // Check every second (20 ticks)
            } else {
                player.sendMessage(prefix + ChatColor.RED + "Home " + ChatColor.AQUA + homeName + ChatColor.RED + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteHome(Player player, String homeName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            int rowsAffected = stmt.executeUpdate();

            // Confirm if the home was actually deleted
            if (rowsAffected == 0) {
                player.sendMessage(prefix + ChatColor.RED + "Home " + ChatColor.AQUA + homeName + ChatColor.RED + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHomeNames(Player player) {
        List<String> homeNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT name FROM homes WHERE player = ?");
            stmt.setString(1, player.getName());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                homeNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homeNames;
    }
}
