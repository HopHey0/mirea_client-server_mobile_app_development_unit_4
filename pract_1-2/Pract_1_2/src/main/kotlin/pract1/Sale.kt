package pract1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val product: String,
    val qty: Int,
    val revenue: Int
)

@Serializable
data class Sale(
    @SerialName(value = "today")
    val day: String,
    val items: List<Item>
)
