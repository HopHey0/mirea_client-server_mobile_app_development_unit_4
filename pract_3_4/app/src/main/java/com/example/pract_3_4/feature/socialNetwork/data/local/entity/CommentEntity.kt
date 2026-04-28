package com.example.pract_3_4.feature.socialNetwork.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class CommentEntity(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String
)
