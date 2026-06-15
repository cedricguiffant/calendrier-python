package com.lolinsight.data.remote

import com.lolinsight.data.remote.dto.DataDragonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DataDragonApi {
    @GET("cdn/{version}/data/fr_FR/champion.json")
    suspend fun getChampions(
        @Path("version") version: String = "14.1.1"
    ): DataDragonResponse

    @GET("api/versions.json")
    suspend fun getVersions(): List<String>

    companion object {
        const val BASE_URL = "https://ddragon.leagueoflegends.com/"
    }
}
