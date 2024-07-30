package one.hyro.api.dto.player

import one.hyro.common.Rank
import java.util.Date
import java.util.UUID

data class PlayerData(
    val uuid: UUID,
    val rank: Rank,
    val firstJoined: Date,
    val lastSeen: Date?
)
