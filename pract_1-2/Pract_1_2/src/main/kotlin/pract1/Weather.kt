package pract1

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val city: String,
    val temp: Int,
    val condition: String
)
