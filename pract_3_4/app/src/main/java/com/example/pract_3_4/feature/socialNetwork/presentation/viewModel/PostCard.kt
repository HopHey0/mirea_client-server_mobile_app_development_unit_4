package com.example.pract_3_4.feature.socialNetwork.presentation.viewModel

import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem

enum class LoadStatus { Loading, Ready, Error }
data class PostCard(
    val isProfPicLoading: LoadStatus = LoadStatus.Loading,
    val post: PostItem,
    val isCommentsLoading: LoadStatus = LoadStatus.Loading,
    val comments: List<CommentItem> = emptyList(),
    val profPicUrl: String? = null
)
