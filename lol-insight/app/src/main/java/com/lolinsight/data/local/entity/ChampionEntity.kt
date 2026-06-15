package com.lolinsight.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lolinsight.domain.model.Champion
import com.lolinsight.domain.model.ChampionRole

@Entity(tableName = "champions")
data class ChampionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String,
    val tags: String, // JSON string of list
    val difficulty: Int
) {
    fun toDomain(): Champion = Champion(
        id = id,
        name = name,
        role = try { ChampionRole.valueOf(role) } catch (e: Exception) { ChampionRole.UNKNOWN },
        tags = tags.split(",").filter { it.isNotEmpty() },
        difficulty = difficulty
    )

    companion object {
        fun fromDomain(champion: Champion): ChampionEntity = ChampionEntity(
            id = champion.id,
            name = champion.name,
            role = champion.role.name,
            tags = champion.tags.joinToString(","),
            difficulty = champion.difficulty
        )
    }
}
