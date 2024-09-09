package one.hyro.spark.lib.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import one.hyro.spark.lib.api.dto.ban.BanData;
import one.hyro.spark.lib.api.dto.ban.BanJson;

import java.util.Date;
import java.util.UUID;

public class BanAdapter {
    @ToJson BanJson toJson(BanData data) {
        return new BanJson(
                data.uuid().toString(),
                data.agent_uuid().toString(),
                data.reason(),
                data.created_at().toString(),
                data.expires_at().toString()
        );
    }

    @FromJson BanData fromJson(BanJson json) {
        return new BanData(
                UUID.fromString(json.uuid()),
                UUID.fromString(json.agent_uuid()),
                json.reason(),
                new Date(json.created_at()),
                new Date(json.expires_at())
        );
    }
}
