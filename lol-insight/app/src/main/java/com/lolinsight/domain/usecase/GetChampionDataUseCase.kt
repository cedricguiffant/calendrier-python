package com.lolinsight.domain.usecase

import com.lolinsight.domain.model.Champion
import com.lolinsight.domain.repository.ChampionRepository
import javax.inject.Inject

class GetChampionDataUseCase @Inject constructor(
    private val championRepository: ChampionRepository
) {
    suspend fun getAllChampions(): List<Champion> = championRepository.getAllChampions()

    suspend fun getChampion(name: String): Champion? = championRepository.getChampionByName(name)

    suspend fun searchChampions(query: String): List<Champion> =
        championRepository.searchChampions(query)

    suspend fun refreshData(): Result<Unit> = championRepository.refreshChampions()
}
