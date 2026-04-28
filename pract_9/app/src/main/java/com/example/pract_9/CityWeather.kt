package com.example.pract_9

data class CityWeather(
    val name: String,
    val temp: String = "",
    val weather: String,
    val state: String = "Ожидание",
    val isLoading: Boolean = false,
    val error: Boolean = false
)

fun List<CityWeather>.toSummaryString(): String {
    val avgTemp = this
        .mapNotNull { it.temp.replace("°C", "").toIntOrNull() }
        .average()
        .toInt()

    return "Итоговый прогноз:\n" +
            this.joinToString(separator = "\n") { item ->
                "${item.name}: ${item.temp}, ${item.weather}"
            } +
            "\n\nСредняя температура: $avgTemp°C"
}

val cityList = listOf<CityWeather>(
    CityWeather("Москва", weather = "Ясно"),
    CityWeather("Лондон", weather = "Дождь"),
    CityWeather("Нью-Йорк", weather = "Дождь"),
//    CityWeather("Белград", "+10°C", "Ясно"),
//    CityWeather("Нови-Сад", "+11°C", "Дождь"),
//    CityWeather("Берлин", "+4°C", "Ясно"),
//    CityWeather("Сидней", "+15°C", "Дождь"),
//    CityWeather("Вашингтон", "-3°C", "Ясно"),
//    CityWeather("Дрезден", "+6°C", "Дождь"),
//    CityWeather("Гонолулу", "+18°C", "Ясно"),
//    CityWeather("Нью-Вегас", "+12°C", "Ясно"),
)