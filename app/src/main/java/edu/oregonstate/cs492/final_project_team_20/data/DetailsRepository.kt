package edu.oregonstate.cs492.final_project_team_20.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class DetailsRepository(
    private val planetService: PlanetService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentPlanet: String? = null
    private var cachedPlanet: List<Planet?> = emptyList()

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()


    suspend fun loadCurrentPlanet(
        name: String?,
        apiKey: String
    ): Result<List<Planet?>> {
        return if (shouldFetch(name)) {
            withContext(ioDispatcher) {
                try {
                    val response = planetService.getPlanetInfo(name, apiKey)
                    Log.d("DetailsRepository", "API response: $response")
                    if (response.isSuccessful) {
                        cachedPlanet = response.body() ?: emptyList()
                        timeStamp = timeSource.markNow()
                        currentPlanet = name
                        Result.success(cachedPlanet)
                    } else {
                        Result.failure(Exception(response.errorBody().toString()))
                    }
                } catch(e: Exception) {
                    Log.e("DetailsRepository", "Error loading planet info", e)
                    Result.failure(e)
                }
            }
        } else {
            Log.d("DetailsRepository", "shouldFetch returned false")
            Result.success(cachedPlanet)
        }
    }


    private fun shouldFetch(name: String?): Boolean =
        cachedPlanet == null
                || name != currentPlanet
                || (timeStamp + cacheMaxAge).hasPassedNow()
}