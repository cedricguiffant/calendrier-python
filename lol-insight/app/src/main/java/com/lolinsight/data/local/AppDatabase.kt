package com.lolinsight.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lolinsight.data.local.dao.AnalysisHistoryDao
import com.lolinsight.data.local.dao.ChampionDao
import com.lolinsight.data.local.entity.AnalysisHistoryEntity
import com.lolinsight.data.local.entity.ChampionEntity

@Database(
    entities = [
        ChampionEntity::class,
        AnalysisHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao
    abstract fun analysisHistoryDao(): AnalysisHistoryDao
}
