package com.lolinsight.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lolinsight.data.local.AppDatabase
import com.lolinsight.data.local.dao.AnalysisHistoryDao
import com.lolinsight.data.local.dao.ChampionDao
import com.lolinsight.data.remote.DataDragonApi
import com.lolinsight.data.repository.AnalysisRepositoryImpl
import com.lolinsight.data.repository.ChampionRepositoryImpl
import com.lolinsight.domain.repository.AnalysisRepository
import com.lolinsight.domain.repository.ChampionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .serializeNulls()
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(DataDragonApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideDataDragonApi(retrofit: Retrofit): DataDragonApi =
        retrofit.create(DataDragonApi::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lol_insight_db"
        ).build()

    @Provides
    @Singleton
    fun provideChampionDao(database: AppDatabase): ChampionDao = database.championDao()

    @Provides
    @Singleton
    fun provideAnalysisHistoryDao(database: AppDatabase): AnalysisHistoryDao =
        database.analysisHistoryDao()

    @Provides
    @Singleton
    fun provideChampionRepository(
        championDao: ChampionDao,
        dataDragonApi: DataDragonApi
    ): ChampionRepository = ChampionRepositoryImpl(championDao, dataDragonApi)

    @Provides
    @Singleton
    fun provideAnalysisRepository(
        analysisHistoryDao: AnalysisHistoryDao,
        gson: Gson
    ): AnalysisRepository = AnalysisRepositoryImpl(analysisHistoryDao, gson)
}
