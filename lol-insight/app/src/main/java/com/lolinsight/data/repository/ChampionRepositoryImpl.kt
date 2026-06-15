package com.lolinsight.data.repository

import com.lolinsight.data.local.dao.ChampionDao
import com.lolinsight.data.local.entity.ChampionEntity
import com.lolinsight.data.local.knowledge.ChampionKnowledge
import com.lolinsight.data.remote.DataDragonApi
import com.lolinsight.domain.model.Champion
import com.lolinsight.domain.model.ChampionRole
import com.lolinsight.domain.repository.ChampionRepository
import javax.inject.Inject

class ChampionRepositoryImpl @Inject constructor(
    private val championDao: ChampionDao,
    private val dataDragonApi: DataDragonApi
) : ChampionRepository {

    override suspend fun getAllChampions(): List<Champion> {
        val cached = championDao.getAllChampions()
        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        // Return local knowledge base champions if DB is empty
        return getLocalChampions()
    }

    override suspend fun getChampionByName(name: String): Champion? {
        val cached = championDao.getChampionByName(name)
        return cached?.toDomain() ?: getLocalChampions().find {
            it.name.equals(name, ignoreCase = true)
        }
    }

    override suspend fun searchChampions(query: String): List<Champion> {
        val cached = championDao.searchChampions(query)
        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }
        return getLocalChampions().filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override suspend fun refreshChampions(): Result<Unit> {
        return try {
            val versions = dataDragonApi.getVersions()
            val latestVersion = versions.firstOrNull() ?: "14.1.1"
            val response = dataDragonApi.getChampions(latestVersion)
            val entities = response.data.values.map {
                ChampionEntity.fromDomain(it.toDomain())
            }
            championDao.deleteAll()
            championDao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getLocalChampions(): List<Champion> {
        return ChampionKnowledge.knowledgeBase.values.map { entry ->
            Champion(
                id = entry.name.lowercase().replace(" ", ""),
                name = entry.name,
                role = parseRole(entry.role),
                tags = listOf(entry.role),
                difficulty = 2
            )
        }
    }

    private fun parseRole(roleStr: String): ChampionRole {
        return when {
            roleStr.contains("TOP", ignoreCase = true) -> ChampionRole.TOP
            roleStr.contains("JUNGLE", ignoreCase = true) -> ChampionRole.JUNGLE
            roleStr.contains("MID", ignoreCase = true) -> ChampionRole.MID
            roleStr.contains("ADC", ignoreCase = true) -> ChampionRole.ADC
            roleStr.contains("SUPPORT", ignoreCase = true) -> ChampionRole.SUPPORT
            else -> ChampionRole.UNKNOWN
        }
    }
}
