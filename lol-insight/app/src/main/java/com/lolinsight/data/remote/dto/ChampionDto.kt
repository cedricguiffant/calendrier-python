package com.lolinsight.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.lolinsight.domain.model.Champion
import com.lolinsight.domain.model.ChampionRole

data class DataDragonResponse(
    @SerializedName("data") val data: Map<String, ChampionDto>
)

data class ChampionDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("info") val info: ChampionInfoDto? = null
) {
    fun toDomain(): Champion = Champion(
        id = id,
        name = name,
        role = inferRole(tags),
        tags = tags,
        difficulty = info?.difficulty ?: 1
    )

    private fun inferRole(tags: List<String>): ChampionRole {
        return when {
            tags.contains("Marksman") -> ChampionRole.ADC
            tags.contains("Support") -> ChampionRole.SUPPORT
            tags.contains("Jungle") -> ChampionRole.JUNGLE
            tags.contains("Assassin") -> ChampionRole.MID
            tags.contains("Mage") -> ChampionRole.MID
            tags.contains("Fighter") -> ChampionRole.TOP
            tags.contains("Tank") -> ChampionRole.TOP
            else -> ChampionRole.UNKNOWN
        }
    }
}

data class ChampionInfoDto(
    @SerializedName("difficulty") val difficulty: Int
)
