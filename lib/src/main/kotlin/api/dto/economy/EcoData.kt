package one.hyro.api.dto.economy

import java.util.UUID

data class EcoData(
    val uuid: UUID,
    val purse: Int,
    val bank: Int
)
