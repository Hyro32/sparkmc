package one.hyro.lib.services;

import one.hyro.lib.i18n.Locales;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayersService {
    public static void createPlayerEntry(UUID uuid) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("uuid", uuid.toString());
            body.put("first_joined", new Date().toString());

            URL request = new URL(ApiEndpoints.PLAYERS.getUrl());
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPLayerEntry(UUID uuid) {
        try {
            URL request = new URL(ApiEndpoints.PLAYERS + "/" + uuid);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            return client.getResponseMessage();
        } catch (IOException e) {
            return null;
        }
    }

    public static void deletePlayerEntry(UUID uuid) {
        try {
            URL request = new URL(ApiEndpoints.PLAYERS + "/" + uuid);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("DELETE");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().flush();
        } catch (IOException e) {}
    }

    public static void updatePlayerEntryLocale(UUID uuid, Locales locale) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("uuid", uuid.toString());
            body.put("locale", locale.toString());

            URL request = new URL(ApiEndpoints.PLAYERS + "/locale");
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (Exception e) {}
    }

    public static void updatePlayerEntryRank(UUID uuid, String rank) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("uuid", uuid.toString());
            body.put("rank", rank);

            URL request = new URL(ApiEndpoints.PLAYERS + "/rank");
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (IOException e) {}
    }

    public static void updatePlayerEntryLastJoined(UUID uuid) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("uuid", uuid.toString());
            body.put("last_joined", new Date().toString());

            URL request = new URL(ApiEndpoints.PLAYERS + "/" + uuid);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (IOException e) {}
    }

    public static void setCachePlayerLocale(UUID uuid, Locales locale) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("uuid", uuid.toString());
            body.put("locale", locale.toString());

            URL request = new URL(ApiEndpoints.PLAYERS + "/locale");
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (Exception e) {}
    }

    public static String getCachePlayerLocale(UUID uuid) {
        try {
            URL request = new URL(ApiEndpoints.PLAYERS + "/locale/" + uuid);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            return client.getResponseMessage();
        } catch (Exception e) {
            return null;
        }
    }
}
