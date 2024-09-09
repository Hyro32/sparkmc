package one.hyro.spark.lib.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import one.hyro.spark.lib.api.adapter.BanAdapter;
import one.hyro.spark.lib.api.adapter.ConnectionsAdapter;
import one.hyro.spark.lib.api.adapter.EconomyAdapter;
import one.hyro.spark.lib.api.adapter.PlayerAdapter;
import one.hyro.spark.lib.api.dto.ban.BanData;
import one.hyro.spark.lib.api.dto.connections.ConnectionsData;
import one.hyro.spark.lib.api.dto.economy.EconomyData;
import one.hyro.spark.lib.api.dto.player.PlayerData;

import java.io.IOException;
import java.util.UUID;

public class SparkApi {
    private static final String BASE_URL = "http://localhost:3000";
    private static final Moshi moshi = new Moshi.Builder()
            .add(new BanAdapter())
            .add(new ConnectionsAdapter())
            .add(new EconomyAdapter())
            .add(new PlayerAdapter())
            .build();

    // Get
    public static BanData getBan(UUID uuid) {
        String response = HttpRequest.get(BASE_URL + "/bans/" + uuid);

        try {
            JsonAdapter<BanData> jsonAdapter = moshi.adapter(BanData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionsData getConnections(UUID uuid) {
        String response = HttpRequest.get(BASE_URL + "/connections/" + uuid);

        try {
            JsonAdapter<ConnectionsData> jsonAdapter = moshi.adapter(ConnectionsData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EconomyData getEconomy(UUID uuid) {
        String response = HttpRequest.get(BASE_URL + "/economy/" + uuid);

        try {
            JsonAdapter<EconomyData> jsonAdapter = moshi.adapter(EconomyData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerData getPlayer(UUID uuid) {
        String response = HttpRequest.get(BASE_URL + "/players/" + uuid);

        try {
            JsonAdapter<PlayerData> jsonAdapter = moshi.adapter(PlayerData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Post
    public static void createBan(BanData banData) {
        String body = moshi.adapter(BanData.class).toJson(banData);
        HttpRequest.post(BASE_URL + "/bans", body);
    }

    public static void createConnections(ConnectionsData connectionsData) {
        String body = moshi.adapter(ConnectionsData.class).toJson(connectionsData);
        HttpRequest.post(BASE_URL + "/connections", body);
    }

    public static void createEconomy(EconomyData economyData) {
        String body = moshi.adapter(EconomyData.class).toJson(economyData);
        HttpRequest.post(BASE_URL + "/economy", body);
    }

    public static void createPlayer(PlayerData playerData) {
        String body = moshi.adapter(PlayerData.class).toJson(playerData);
        HttpRequest.post(BASE_URL + "/players", body);
    }

    // Put / Patch
    public static BanData updateBan(UUID uuid, BanData banData) {
        String body = moshi.adapter(BanData.class).toJson(banData);
        String response = HttpRequest.patch(BASE_URL + "/bans/" + uuid, body);

        try {
            JsonAdapter<BanData> jsonAdapter = moshi.adapter(BanData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionsData updateConnections(UUID uuid, ConnectionsData connectionsData) {
        String body = moshi.adapter(ConnectionsData.class).toJson(connectionsData);
        String response = HttpRequest.patch(BASE_URL + "/connections/" + uuid, body);

        try {
            JsonAdapter<ConnectionsData> jsonAdapter = moshi.adapter(ConnectionsData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EconomyData updateEconomy(UUID uuid, EconomyData economyData) {
        String body = moshi.adapter(EconomyData.class).toJson(economyData);
        String response = HttpRequest.patch(BASE_URL + "/economy/" + uuid, body);

        try {
            JsonAdapter<EconomyData> jsonAdapter = moshi.adapter(EconomyData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerData updatePlayer(UUID uuid, PlayerData playerData) {
        String body = moshi.adapter(PlayerData.class).toJson(playerData);
        String response = HttpRequest.patch(BASE_URL + "/players/" + uuid, body);

        try {
            JsonAdapter<PlayerData> jsonAdapter = moshi.adapter(PlayerData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete
    public static void deleteBan(UUID uuid) {
        HttpRequest.delete(BASE_URL + "/bans/" + uuid);
    }

    public static void deleteConnections(UUID uuid) {
        HttpRequest.delete(BASE_URL + "/connections/" + uuid);
    }

    public static void deleteEconomy(UUID uuid) {
        HttpRequest.delete(BASE_URL + "/economy/" + uuid);
    }

    public static void deletePlayer(UUID uuid) {
        HttpRequest.delete(BASE_URL + "/players/" + uuid);
    }
}
