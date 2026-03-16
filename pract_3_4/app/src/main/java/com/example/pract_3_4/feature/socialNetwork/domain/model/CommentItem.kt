package com.example.pract_3_4.feature.socialNetwork.domain.model

data class CommentItem(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String
)
