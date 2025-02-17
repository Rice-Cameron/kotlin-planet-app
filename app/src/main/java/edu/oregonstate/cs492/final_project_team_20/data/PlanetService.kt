package edu.oregonstate.cs492.final_project_team_20.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetService {

    @GET("planets")
    suspend fun getPlanetInfo(
        @Query("name") name: String?,
        @Query("X-Api-Key") apiKey: String
    ): Response<List<Planet>>

    companion object {
        private const val BASE_URL = "https://api.api-ninjas.com/v1/"
        fun create() : PlanetService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(PlanetService::class.java)
        }
    }
}