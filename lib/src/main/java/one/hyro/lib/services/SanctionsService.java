package one.hyro.lib.services;

import one.hyro.lib.enums.SanctionTypes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SanctionsService {
    public static void createSanctionEntry(UUID uuid, UUID agentUuid, SanctionTypes sanctionType, Date sanctionExpiration) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("target_uuid", uuid.toString());
            body.put("agent_uuid", agentUuid.toString());
            body.put("type", sanctionType.toString());
            body.put("date", new Date().toString());
            body.put("expiration", sanctionExpiration.toString());

            URL request = new URL(ApiEndpoints.SANCTIONS.getUrl());
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
        } catch (IOException e) {}
    }

    public static String getAllSanctions() {
        try {
            URL request = new URL(ApiEndpoints.SANCTIONS.getUrl());
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            return client.getResponseMessage();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getSanctionEntryById(int id) {
        try {
            URL request = new URL(ApiEndpoints.SANCTIONS + "/" + id);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            return client.getResponseMessage();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getSanctionEntryByUuidAndType(UUID uuid, SanctionTypes sanctionType) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("target_uuid", uuid.toString());
            body.put("type", sanctionType.toString());

            URL request = new URL(ApiEndpoints.SANCTIONS.getUrl());
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().write(body.toString().getBytes());
            client.getOutputStream().flush();
            return client.getResponseMessage();
        } catch (IOException e) {
            return null;
        }
    }

    public static void deleteSanctionEntry(int id) {
        try {
            URL request = new URL(ApiEndpoints.SANCTIONS + "/" + id);
            HttpURLConnection client = (HttpURLConnection) request.openConnection();
            client.setRequestMethod("DELETE");
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.getOutputStream().flush();
        } catch (IOException e) {}
    }
}
