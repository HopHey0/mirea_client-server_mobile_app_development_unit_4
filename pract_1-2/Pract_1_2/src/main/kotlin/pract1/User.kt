package pract1

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String)