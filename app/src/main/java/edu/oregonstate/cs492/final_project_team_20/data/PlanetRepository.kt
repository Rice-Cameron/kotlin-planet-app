package edu.oregonstate.cs492.final_project_team_20.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class PlanetRepository(
    private val service: PlanetService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getPlanetNames(): List<String> {
        return withContext(ioDispatcher) {
            try {
                val planetNames = listOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")
                planetNames
            } catch(e: Exception) {
                emptyList()
            }
        }
    }
}