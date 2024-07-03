package one.hyro.utils;

import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class HyroApi {
    private static final String BASE_URL = "http://localhost:3000";

    public static JSONObject createPlayer(UUID uuid) {
        return ApiRequests.sendPostRequest(BASE_URL + "/players", new JSONObject().put("uuid", uuid.toString()));
    }

    public static JSONObject getPlayer(UUID uuid) {
        return ApiRequests.sendGetRequest(BASE_URL + "/players/" + uuid.toString());
    }

    public static JSONObject createBan(UUID uuid, UUID operator, String reason, Date expiration) {
        JSONObject ban = new JSONObject();
        ban.put("uuid", uuid.toString());
        ban.put("operator", operator.toString());
        ban.put("reason", reason);
        ban.put("expiration", expiration.getTime());
        return ApiRequests.sendPostRequest(BASE_URL + "/ban", ban);
    }

    public static JSONObject getBan(UUID uuid) {
        return ApiRequests.sendGetRequest(BASE_URL + "/ban/" + uuid);
    }

    public static void deleteBan(UUID uuid) {
        ApiRequests.sendDeleteRequest(BASE_URL + "/ban/" + uuid);
    }
}