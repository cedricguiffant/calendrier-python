package com.lolinsight.domain.repository

import com.lolinsight.domain.model.Champion

interface ChampionRepository {
    suspend fun getAllChampions(): List<Champion>
    suspend fun getChampionByName(name: String): Champion?
    suspend fun searchChampions(query: String): List<Champion>
    suspend fun refreshChampions(): Result<Unit>
}
