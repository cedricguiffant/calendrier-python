package com.lolinsight.domain.model

import java.util.Date

data class GameAnalysis(
    val id: Long = 0,
    val allyTeam: List<String>,
    val enemyTeam: List<String>,
    val matchups: List<Matchup>,
    val threats: List<Threat>,
    val allySynergies: List<Synergy>,
    val enemySynergies: List<Synergy>,
    val generalAdvice: List<String>,
    val earlyGameAdvice: List<String>,
    val midGameAdvice: List<String>,
    val teamfightAdvice: List<String>,
    val itemRecommendations: List<String>,
    val timestamp: Date = Date()
)
