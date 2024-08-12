package one.hyro.api

import api.ApiRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import one.hyro.api.adapter.EcoAdapter
import one.hyro.api.adapter.PlayerAdapter
import one.hyro.api.dto.economy.EcoData
import one.hyro.api.dto.player.PlayerData
import one.hyro.common.Rank
import java.util.Date
import java.util.UUID

object PlanetApi {
    private const val BASE_URL = "http://localhost:3000"
    private val moshi = Moshi.Builder()
        .add(PlayerAdapter())
        .add(EcoAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val players = PlayersApi
    val moderation = ModerationApi
    val eco = EconomyApi

    object PlayersApi {
        fun get(uuid: UUID): PlayerData {
            val response: String = ApiRequest.get("$BASE_URL/players/$uuid")
            return moshi.adapter(PlayerData::class.java).fromJson(response)!!
        }

        fun getAll(): List<PlayerData> {
            val response: String = ApiRequest.get("$BASE_URL/players")
            return listOf(moshi.adapter(PlayerData::class.java).fromJson(response)!!)
        }

        fun update(uuid: UUID, playerData: PlayerData): PlayerData {
            ApiRequest.patch("$BASE_URL/players/$uuid", moshi.adapter(PlayerData::class.java).toJson(playerData))
            return get(uuid)
        }

        fun create(uuid: UUID) {
            val playerData = moshi.adapter(PlayerData::class.java).toJson(PlayerData(uuid, Rank.USER, Date(), null))
            ApiRequest.post("$BASE_URL/players", playerData)
        }

        fun create(uuid: UUID, rank: Rank) {
            val playerData = moshi.adapter(PlayerData::class.java).toJson(PlayerData(uuid, rank, Date(), null))
            ApiRequest.post("$BASE_URL/players", playerData)
        }

        fun delete(uuid: UUID) = ApiRequest.delete("$BASE_URL/players/$uuid")
    }

    object ModerationApi {
        // Add moderation-related API calls here
    }

    object EconomyApi {
        fun get(uuid: UUID): EcoData {
            val response: String = ApiRequest.get("$BASE_URL/eco/$uuid")
            return moshi.adapter(EcoData::class.java).fromJson(response)!!
        }

        fun getAll(): List<EcoData> {
            val response: String = ApiRequest.get("$BASE_URL/eco")
            return listOf(moshi.adapter(EcoData::class.java).fromJson(response)!!)
        }

        fun update(uuid: UUID, ecoData: EcoData): EcoData {
            ApiRequest.patch("$BASE_URL/eco/$uuid", moshi.adapter(EcoData::class.java).toJson(ecoData))
            return get(uuid)
        }
    }
}