package one.hyro.spark.lib.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import one.hyro.spark.lib.api.dto.player.PlayerData;
import one.hyro.spark.lib.api.dto.player.PlayerJson;
import one.hyro.spark.lib.common.Rank;

import java.util.Date;
import java.util.UUID;

public class PlayerAdapter {
    @ToJson PlayerJson toJson(PlayerData data) {
        return new PlayerJson(
                data.uuid().toString(),
                data.rank().toString(),
                data.rank_expiration().toString(),
                data.first_joined().toString(),
                data.last_seen().toString()
        );
    }

    @FromJson PlayerData fromJson(PlayerJson json) {
        return new PlayerData(
                UUID.fromString(json.uuid()),
                Rank.valueOf(json.rank()),
                new Date(json.rank_expiration()),
                new Date(json.first_joined()),
                new Date(json.last_seen())
        );
    }
}
