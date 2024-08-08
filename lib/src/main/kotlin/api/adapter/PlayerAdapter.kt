package one.hyro.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import one.hyro.api.dto.player.PlayerData
import one.hyro.api.dto.player.PlayerJson
import one.hyro.common.Rank
import java.util.*

class PlayerAdapter {
    @ToJson
    fun toJson(data: PlayerData): PlayerJson {
        return PlayerJson(
            data.uuid.toString(),
            data.rank.toString(),
            data.firstJoined.toString(),
            data.lastSeen.toString()
        )
    }

    @FromJson
    fun fromJson(json: PlayerJson): PlayerData {
        return PlayerData(
            UUID.fromString(json.uuid),
            Rank.valueOf(json.rank),
            Date(json.firstJoined),
            Date(json.lastSeen)
        )
    }
}