package com.lolinsight.data.repository

import com.google.gson.Gson
import com.lolinsight.data.local.dao.AnalysisHistoryDao
import com.lolinsight.data.local.entity.AnalysisHistoryEntity
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.repository.AnalysisRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnalysisRepositoryImpl @Inject constructor(
    private val analysisHistoryDao: AnalysisHistoryDao,
    private val gson: Gson
) : AnalysisRepository {

    override suspend fun saveAnalysis(analysis: GameAnalysis): Long {
        val entity = AnalysisHistoryEntity.fromDomain(analysis, gson)
        val id = analysisHistoryDao.insert(entity)
        analysisHistoryDao.pruneOldEntries()
        return id
    }

    override suspend fun getAnalysisById(id: Long): GameAnalysis? {
        return analysisHistoryDao.getAnalysisById(id)?.toDomain(gson)
    }

    override fun getAllAnalyses(): Flow<List<GameAnalysis>> {
        return analysisHistoryDao.getAllAnalyses().map { entities ->
            entities.map { it.toDomain(gson) }
        }
    }

    override suspend fun deleteAnalysis(id: Long) {
        analysisHistoryDao.deleteById(id)
    }

    override suspend fun getAnalysisCount(): Int {
        return analysisHistoryDao.getCount()
    }
}
