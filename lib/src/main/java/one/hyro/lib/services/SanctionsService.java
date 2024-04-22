package one.hyro.lib.services;

import com.google.gson.JsonObject;
import one.hyro.lib.enums.SanctionTypes;
import one.hyro.lib.utils.Formatter;
import one.hyro.lib.utils.ApiRequest;

import java.util.Date;
import java.util.UUID;

public class SanctionsService {
    public static void createSanctionEntry(UUID uuid, UUID agentUuid, SanctionTypes sanctionType, String reason, Date sanctionExpiration) {
        JsonObject body = new JsonObject();
        body.addProperty("target_uuid", uuid.toString());
        body.addProperty("agent_uuid", agentUuid.toString());
        body.addProperty("type", sanctionType.toString());
        body.addProperty("reason", reason);
        body.addProperty("date", Formatter.formatIsoDate(new Date()));
        body.addProperty("expiration", Formatter.formatIsoDate(sanctionExpiration));

        ApiRequest.POST("http://localhost:3000/sanctions", body);
    }

    public static String getAllSanctions() {
        return ApiRequest.GET("http://localhost:3000/sanctions", null);
    }

    public static String getSanctionEntryById(int id) {
        return ApiRequest.GET("http://localhost:3000/sanctions/" + id, null);
    }

    public static String getSanctionEntryByUuidAndType(UUID uuid, SanctionTypes sanctionType) {
        JsonObject body = new JsonObject();
        body.addProperty("target_uuid", uuid.toString());
        body.addProperty("type", sanctionType.toString());

        return ApiRequest.GET("http://localhost:3000/sanctions", null);
    }

    public static void deleteSanctionEntry(int id) {
        ApiRequest.DELETE("http://localhost:3000/sanctions/" + id);
    }
}
