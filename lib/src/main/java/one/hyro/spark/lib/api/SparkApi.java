package one.hyro.spark.lib.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import one.hyro.spark.lib.api.adapter.BanAdapter;
import one.hyro.spark.lib.api.adapter.ConnectionsAdapter;
import one.hyro.spark.lib.api.adapter.EconomyAdapter;
import one.hyro.spark.lib.api.adapter.PlayerAdapter;
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

    public static PlayerData getPlayer(UUID uuid) {
        String response = HttpRequest.get(BASE_URL + "/players/" + uuid);

        try {
            JsonAdapter<PlayerData> jsonAdapter = moshi.adapter(PlayerData.class);
            return jsonAdapter.fromJson(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
