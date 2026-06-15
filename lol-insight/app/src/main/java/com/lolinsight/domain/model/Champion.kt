package com.lolinsight.domain.model

data class Champion(
    val id: String,
    val name: String,
    val role: ChampionRole,
    val tags: List<String> = emptyList(),
    val difficulty: Int = 1 // 1-3
)

enum class ChampionRole {
    TOP, JUNGLE, MID, ADC, SUPPORT, UNKNOWN
}
