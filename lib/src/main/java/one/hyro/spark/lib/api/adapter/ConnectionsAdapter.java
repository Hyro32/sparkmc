package one.hyro.spark.lib.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import one.hyro.spark.lib.api.dto.connections.ConnectionsData;
import one.hyro.spark.lib.api.dto.connections.ConnectionsJson;

import java.util.UUID;

public class ConnectionsAdapter {
    @ToJson ConnectionsJson toJson(ConnectionsData data) {
        return new ConnectionsJson(
                data.uuid().toString(),
                data.discord()
        );
    }

    @FromJson ConnectionsData fromJson(ConnectionsJson json) {
        return new ConnectionsData(
                UUID.fromString(json.uuid()),
                json.discord()
        );
    }
}
