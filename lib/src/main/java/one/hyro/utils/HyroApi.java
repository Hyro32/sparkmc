package one.hyro.utils;

import org.json.JSONObject;

import java.util.UUID;

public class HyroApi {
    private static final String BASE_URL = "http://localhost:3000";

    public static JSONObject createPlayer(UUID uuid) {
        return ApiRequests.sendPostRequest(BASE_URL + "/players", new JSONObject().put("uuid", uuid.toString()));
    }

    public static JSONObject getPlayer(UUID uuid) {
        return ApiRequests.sendGetRequest(BASE_URL + "/players/" + uuid.toString());
    }
}