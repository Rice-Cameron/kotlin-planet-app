package edu.oregonstate.cs492.final_project_team_20.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


// Data class for a planet, use MOSHI to parse JSON from API to this class
// Add more fields as needed, especially distance from Earth

@JsonClass(generateAdapter = true)
data class Planet (
   @Json(name="name") val name: String,
   @Json(name="distance_light_year") val distance: Double,
   @Json(name="temperature") val temperature: Double,
   @Json(name="mass") val mass: Double,
   @Json(name="semi_major_axis") val semi_major_axis: Double,
   @Json(name="period") val orbital_period: Double
)