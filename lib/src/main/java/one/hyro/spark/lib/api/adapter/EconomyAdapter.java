package one.hyro.spark.lib.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import one.hyro.spark.lib.api.dto.economy.EconomyData;
import one.hyro.spark.lib.api.dto.economy.EconomyJson;

import java.util.UUID;

public class EconomyAdapter {
    @ToJson EconomyJson toJson(EconomyData data) {
        return new EconomyJson(
                data.uuid().toString(),
                data.purse(),
                data.bank()
        );
    }

    @FromJson EconomyData fromJson(EconomyJson json) {
        return new EconomyData(
                UUID.fromString(json.uuid()),
                json.purse(),
                json.bank()
        );
    }
}
