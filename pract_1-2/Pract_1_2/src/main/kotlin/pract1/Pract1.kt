package pract1

import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        runBlocking {
            val users: Deferred<List<String>?> = async { getUsers() }
            val sales: Deferred<Map<String, Int>?> = async { getSales() }
            val weather: Deferred<List<String>?> = async { getWeather() }
            println(users.await())
            println(sales.await())
            println(weather.await())
        }
    }
    println(time / 1000.0)
}

suspend fun getUsers(): List<String>? =
    coroutineScope {
        delay(1800)
        try {
            val jsonString = object {}.javaClass.getResource("users.json")?.readText()
                ?: throw IllegalArgumentException("Файл users не найден в resources")
            val jsonUsers = Json.decodeFromString<List<User>>(jsonString)
            val usersFormatted = jsonUsers.map {
                it.name
            }
            usersFormatted
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
    }

suspend fun getSales(): Map<String, Int>? =
    coroutineScope {
        delay(1200)
        try {
            val jsonString = object {}.javaClass.getResource("/sales.json")?.readText()
                ?: throw IllegalArgumentException("Файл sales не найден в resources")
            val sale = Json.decodeFromString<Sale>(jsonString)
            val salesStat = mutableMapOf<String, Int>()
            salesStat[sale.day] = sale.items.sumOf { it.revenue }
            salesStat
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
    }

suspend fun getWeather(): List<String>? =
    coroutineScope {
        delay(2500)
        try {
            val jsonString = object {}.javaClass.getResource("/weather.json")?.readText()
                ?: throw IllegalArgumentException("Файл weather не найден в resources")
            val weather = Json.decodeFromString<List<Weather>>(jsonString)
            val weatherFormattedList = weather.map {
                "${it.city}: ${it.temp}°C"
            }
            weatherFormattedList
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
    }