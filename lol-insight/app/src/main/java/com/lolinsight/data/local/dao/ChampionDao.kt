package com.lolinsight.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lolinsight.data.local.entity.ChampionEntity

@Dao
interface ChampionDao {
    @Query("SELECT * FROM champions")
    suspend fun getAllChampions(): List<ChampionEntity>

    @Query("SELECT * FROM champions WHERE name = :name LIMIT 1")
    suspend fun getChampionByName(name: String): ChampionEntity?

    @Query("SELECT * FROM champions WHERE name LIKE '%' || :query || '%'")
    suspend fun searchChampions(query: String): List<ChampionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(champions: List<ChampionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(champion: ChampionEntity)

    @Query("DELETE FROM champions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM champions")
    suspend fun getCount(): Int
}
