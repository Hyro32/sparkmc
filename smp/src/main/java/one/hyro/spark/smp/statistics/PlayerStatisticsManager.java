package one.hyro.spark.smp.statistics;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public class PlayerStatisticsManager {

    private final JavaPlugin plugin;
    private Connection connection;
    private final String databaseUrl;

    public PlayerStatisticsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        File dbFile = new File(plugin.getDataFolder(), "statistics.db");
        this.databaseUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connect();
        createTable();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_statistics (" +
                "player_uuid TEXT PRIMARY KEY," +
                "deaths INTEGER DEFAULT 0," +
                "kills INTEGER DEFAULT 0," +
                "kilometers_walked REAL DEFAULT 0.0," +
                "netherite_ingots_crafted INTEGER DEFAULT 0," +
                "diamonds_mined INTEGER DEFAULT 0," +
                "end_visits INTEGER DEFAULT 0" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeaths(Player player) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, deaths) VALUES (?, 1) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET deaths = deaths + 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKills(Player player) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, kills) VALUES (?, 1) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET kills = kills + 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKilometersWalked(Player player, double distance) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, kilometers_walked) VALUES (?, ?) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET kilometers_walked = kilometers_walked + ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.setDouble(2, distance);
            pstmt.setDouble(3, distance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNetheriteIngotsCrafted(Player player) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, netherite_ingots_crafted) VALUES (?, 1) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET netherite_ingots_crafted = netherite_ingots_crafted + 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDiamondsMined(Player player) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, diamonds_mined) VALUES (?, 1) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET diamonds_mined = diamonds_mined + 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEndVisits(Player player) {
        String uuid = player.getUniqueId().toString();
        String updateSQL = "INSERT INTO player_statistics (player_uuid, end_visits) VALUES (?, 1) " +
                "ON CONFLICT(player_uuid) DO UPDATE SET end_visits = end_visits + 1";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStatistics(Player player) {
        StringBuilder stats = new StringBuilder();
        String uuid = player.getUniqueId().toString();

        String querySQL = "SELECT * FROM player_statistics WHERE player_uuid = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.append("Deaths: ").append(rs.getInt("deaths")).append("\n");
                stats.append("Kills: ").append(rs.getInt("kills")).append("\n");
                stats.append("Kilometers Walked: ").append(rs.getDouble("kilometers_walked")).append("\n");
                stats.append("Netherite Ingots Crafted: ").append(rs.getInt("netherite_ingots_crafted")).append("\n");
                stats.append("Diamonds Mined: ").append(rs.getInt("diamonds_mined")).append("\n");
                stats.append("End Visits: ").append(rs.getInt("end_visits")).append("\n");
            } else {
                stats.append("No statistics found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            stats.append("Error retrieving statistics.");
        }
        return stats.toString();
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
