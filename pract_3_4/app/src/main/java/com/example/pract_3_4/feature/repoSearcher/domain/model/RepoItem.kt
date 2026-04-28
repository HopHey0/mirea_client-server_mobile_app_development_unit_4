package com.example.pract_3_4.feature.repoSearcher.domain.model


data class RepoItem(
    val id: Int,
    val fullName: String,
    val description: String,
    val starsCount: Int,
    val language: String
)
