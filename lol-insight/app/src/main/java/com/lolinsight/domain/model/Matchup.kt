package com.lolinsight.domain.model

data class Matchup(
    val champion: String,
    val opponent: String,
    val difficulty: MatchupDifficulty,
    val tips: List<String> = emptyList(),
    val keyItems: List<String> = emptyList()
)

enum class MatchupDifficulty {
    EASY, MEDIUM, HARD;

    fun label(): String = when (this) {
        EASY -> "Facile"
        MEDIUM -> "Moyen"
        HARD -> "Difficile"
    }
}
