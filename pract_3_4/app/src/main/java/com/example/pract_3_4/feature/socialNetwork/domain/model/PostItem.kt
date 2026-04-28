package com.example.pract_3_4.feature.socialNetwork.domain.model

data class PostItem(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val avatarUrl: String
)
