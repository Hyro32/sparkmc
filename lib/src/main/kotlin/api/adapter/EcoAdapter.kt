package one.hyro.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import one.hyro.api.dto.economy.EcoData
import one.hyro.api.dto.economy.EcoJson
import java.util.*

class EcoAdapter {
    @ToJson
    fun toJson(data: EcoData): EcoJson {
        return EcoJson(
            data.uuid.toString(),
            data.bank,
            data.purse
        )
    }

    @FromJson
    fun fromJson(json: EcoJson): EcoData {
        return EcoData(
            UUID.fromString(json.uuid),
            json.bank,
            json.purse
        )
    }
}