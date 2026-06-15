package com.lolinsight.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.model.Matchup
import com.lolinsight.domain.model.Synergy
import com.lolinsight.domain.model.Threat
import java.util.Date

@Entity(tableName = "analysis_history")
data class AnalysisHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val allyTeamJson: String,
    val enemyTeamJson: String,
    val matchupsJson: String,
    val threatsJson: String,
    val allySynergiesJson: String,
    val enemySynergiesJson: String,
    val generalAdviceJson: String,
    val earlyGameAdviceJson: String,
    val midGameAdviceJson: String,
    val teamfightAdviceJson: String,
    val itemRecommendationsJson: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(gson: Gson): GameAnalysis {
        val listType = object : TypeToken<List<String>>() {}.type
        val matchupType = object : TypeToken<List<Matchup>>() {}.type
        val threatType = object : TypeToken<List<Threat>>() {}.type
        val synergyType = object : TypeToken<List<Synergy>>() {}.type

        return GameAnalysis(
            id = id,
            allyTeam = gson.fromJson(allyTeamJson, listType),
            enemyTeam = gson.fromJson(enemyTeamJson, listType),
            matchups = gson.fromJson(matchupsJson, matchupType),
            threats = gson.fromJson(threatsJson, threatType),
            allySynergies = gson.fromJson(allySynergiesJson, synergyType),
            enemySynergies = gson.fromJson(enemySynergiesJson, synergyType),
            generalAdvice = gson.fromJson(generalAdviceJson, listType),
            earlyGameAdvice = gson.fromJson(earlyGameAdviceJson, listType),
            midGameAdvice = gson.fromJson(midGameAdviceJson, listType),
            teamfightAdvice = gson.fromJson(teamfightAdviceJson, listType),
            itemRecommendations = gson.fromJson(itemRecommendationsJson, listType),
            timestamp = Date(timestamp)
        )
    }

    companion object {
        fun fromDomain(analysis: GameAnalysis, gson: Gson): AnalysisHistoryEntity {
            return AnalysisHistoryEntity(
                id = analysis.id,
                allyTeamJson = gson.toJson(analysis.allyTeam),
                enemyTeamJson = gson.toJson(analysis.enemyTeam),
                matchupsJson = gson.toJson(analysis.matchups),
                threatsJson = gson.toJson(analysis.threats),
                allySynergiesJson = gson.toJson(analysis.allySynergies),
                enemySynergiesJson = gson.toJson(analysis.enemySynergies),
                generalAdviceJson = gson.toJson(analysis.generalAdvice),
                earlyGameAdviceJson = gson.toJson(analysis.earlyGameAdvice),
                midGameAdviceJson = gson.toJson(analysis.midGameAdvice),
                teamfightAdviceJson = gson.toJson(analysis.teamfightAdvice),
                itemRecommendationsJson = gson.toJson(analysis.itemRecommendations),
                timestamp = analysis.timestamp.time
            )
        }
    }
}
