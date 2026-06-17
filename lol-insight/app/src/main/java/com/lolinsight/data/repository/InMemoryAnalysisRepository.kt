package com.lolinsight.data.repository

import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.repository.AnalysisRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryAnalysisRepository @Inject constructor() : AnalysisRepository {

    private val nextId = AtomicLong(1L)
    private val _analyses = MutableStateFlow<List<GameAnalysis>>(emptyList())

    override suspend fun saveAnalysis(analysis: GameAnalysis): Long {
        val id = nextId.getAndIncrement()
        val saved = analysis.copy(id = id)
        val updated = listOf(saved) + _analyses.value
        _analyses.value = if (updated.size > 50) updated.take(50) else updated
        return id
    }

    override suspend fun getAnalysisById(id: Long): GameAnalysis? =
        _analyses.value.find { it.id == id }

    override fun getAllAnalyses(): Flow<List<GameAnalysis>> = _analyses.asStateFlow()

    override suspend fun deleteAnalysis(id: Long) {
        _analyses.value = _analyses.value.filter { it.id != id }
    }

    override suspend fun getAnalysisCount(): Int = _analyses.value.size
}
