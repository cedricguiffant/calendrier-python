package com.lolinsight.domain.repository

import com.lolinsight.domain.model.GameAnalysis
import kotlinx.coroutines.flow.Flow

interface AnalysisRepository {
    suspend fun saveAnalysis(analysis: GameAnalysis): Long
    suspend fun getAnalysisById(id: Long): GameAnalysis?
    fun getAllAnalyses(): Flow<List<GameAnalysis>>
    suspend fun deleteAnalysis(id: Long)
    suspend fun getAnalysisCount(): Int
}
