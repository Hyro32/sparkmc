package one.hyro.spark.smp.managers;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.smp.SparkSmp;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeManager {

    @Getter
    private final File dbFile;
    private static final int TELEPORT_COUNTDOWN = 3;

    public HomeManager(File dbFile) {
        this.dbFile = dbFile;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            Statement statement = connection.createStatement();
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
            e.fillInStackTrace();
        }
    }

    public void setHome(Player player, String homeName) {
        List<String> homeNames = getHomeNames(player);
        int maxHomes = getMaxHomes(player);

        if (homeNames.contains(homeName)) {
            Component errorMessage = Component.translatable("context.error.homeAlreadyExists")
                    .args(Component.text(homeName, NamedTextColor.AQUA))
                    .color(NamedTextColor.RED);
            player.sendActionBar(errorMessage);
            return;
        }

        if (homeNames.size() >= maxHomes) {
            Component maxReachedMessage = Component.translatable("context.error.homeMaxReached")
                    .args(Component.text(maxHomes))
                    .color(NamedTextColor.RED);
            player.sendActionBar(maxReachedMessage);
            return;
        }

        player.sendActionBar(Component.translatable("context.success.homeCreated")
                .args(Component.text(homeName, NamedTextColor.AQUA))
                .color(NamedTextColor.GREEN));

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
            stmt.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
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
                        SparkSmp.getInstance().getServer().getWorld(rs.getString("world")),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z"),
                        rs.getFloat("yaw"),
                        rs.getFloat("pitch")
                );

                new BukkitRunnable() {
                    private int countdown = TELEPORT_COUNTDOWN;
                    private final Location initialLocation = player.getLocation().clone();

                    @Override
                    public void run() {
                        if (!player.isOnline() || !player.getLocation().equals(initialLocation)) {
                            Component movedMessage = Component.translatable("context.error.homeMoved")
                                    .color(NamedTextColor.RED);
                            player.sendActionBar(movedMessage);
                            cancel();
                            return;
                        }

                        if (countdown > 0) {
                            Component teleportingMessage = Component.translatable("context.success.homeTeleporting")
                                    .args(Component.text(countdown, NamedTextColor.AQUA))
                                    .color(NamedTextColor.GREEN);
                            player.sendActionBar(teleportingMessage);
                            countdown--;
                        } else {
                            player.teleport(loc);
                            Component successMessage = Component.translatable("context.success.homeSuccess")
                                    .color(NamedTextColor.GOLD);
                            player.sendActionBar(successMessage);
                            cancel();
                        }
                    }
                }.runTaskTimer(SparkSmp.getInstance(), 0L, 20L);
            } else {
                Component notExistMessage = Component.translatable("context.error.homeNotExisting")
                        .args(Component.text(homeName, NamedTextColor.AQUA))
                        .color(NamedTextColor.RED);
                player.sendActionBar(notExistMessage);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public void deleteHome(Player player, String homeName) {
        boolean homeExists = false;

        // Check if the home exists
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                homeExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!homeExists) {
            // Home does not exist, send an error message
            Component notExistMessage = Component.translatable("context.error.homeNotExisting")
                    .args(Component.text(homeName, NamedTextColor.AQUA))
                    .color(NamedTextColor.RED);
            player.sendActionBar(notExistMessage);
            return;
        }

        // Proceed with deletion
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Send success message
        Component deleteMessage = Component.translatable("context.success.homeDeleted")
                .args(Component.text(homeName, NamedTextColor.AQUA))
                .color(NamedTextColor.GREEN);
        player.sendActionBar(deleteMessage);
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
            e.fillInStackTrace();
        }
        return homeNames;
    }

    private int getMaxHomes(Player player) {
        for (int i = 100; i > 0; i--) {
            if (player.hasPermission("sparkmc.maxhomes." + i)) {
                return i;
            }
        }
        return 2;
    }
}
