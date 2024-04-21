package one.hyro.lib.services;

import com.google.gson.JsonObject;
import one.hyro.lib.i18n.Locales;
import one.hyro.lib.utils.Formatter;
import one.hyro.lib.utils.ApiRequest;

import java.util.Date;
import java.util.UUID;

public class PlayersService {
    public static void createPlayerEntry(UUID uuid) {
        JsonObject body = new JsonObject();
        body.addProperty("uuid", uuid.toString());
        body.addProperty("first_joined", Formatter.formatIsoDate(new Date()));

        ApiRequest.POST("http://localhost:3000/players", body);
    }

    public static String getPLayerEntry(UUID uuid) {
        return ApiRequest.GET("http://localhost:3000/players/" + uuid, null);
    }

    public static void deletePlayerEntry(UUID uuid) {
        ApiRequest.DELETE("http://localhost:3000/players/" + uuid);
    }

    public static void updatePlayerEntryLocale(UUID uuid, Locales locale) {
        JsonObject body = new JsonObject();
        body.addProperty("uuid", uuid.toString());
        body.addProperty("locale", locale.toString());

        ApiRequest.PATCH("http://localhost:3000/players/" + uuid, body);
    }

    public static void updatePlayerEntryRank(UUID uuid, String rank) {
        JsonObject body = new JsonObject();
        body.addProperty("uuid", uuid.toString());
        body.addProperty("rank", rank);

        ApiRequest.PATCH("http://localhost:3000/players/" + uuid, body);
    }

    public static void updatePlayerEntryLastJoined(UUID uuid) {
        JsonObject body = new JsonObject();
        body.addProperty("last_joined", Formatter.formatIsoDate(new Date()));

        ApiRequest.PATCH("http://localhost:3000/players/" + uuid, body);
    }

    public static void setCachePlayerLocale(UUID uuid, Locales locale) {
        JsonObject body = new JsonObject();
        body.addProperty("uuid", uuid.toString());
        body.addProperty("locale", locale.toString());

        ApiRequest.POST("http://localhost:3000/players/locale", body);
    }

    public static String getCachePlayerLocale(UUID uuid) {
        return ApiRequest.GET("http://localhost:3000/players/locale/" + uuid, null);
    }
}
