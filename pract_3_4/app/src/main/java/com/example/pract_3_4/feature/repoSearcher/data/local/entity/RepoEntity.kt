package com.example.pract_3_4.feature.repoSearcher.data.local.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoEntity(
    val id: Int,
    @SerialName(value = "full_name")
    val fullName: String,
    val description: String,
    @SerialName(value = "stargazers_count")
    val starsCount: Int,
    val language: String
)