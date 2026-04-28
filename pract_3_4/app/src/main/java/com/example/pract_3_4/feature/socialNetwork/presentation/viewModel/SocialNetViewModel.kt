package com.example.pract_3_4.feature.socialNetwork.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetCommentsUseCase
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class SocialNetViewModel(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {
    val postCards = MutableStateFlow<List<PostCard>>(emptyList())
    val isRefreshing = MutableStateFlow(false)
    var loadPostsJob: Job? = null
//    init {
//        loadPosts()
//    }

    fun refreshPosts(){
        loadPostsJob?.cancel()
        isRefreshing.value = true
        loadPosts()
    }

    fun loadPosts(){
        loadPostsJob = viewModelScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    getPostsUseCase.invoke()
                }
                val comments = withContext(Dispatchers.IO) {
                    getCommentsUseCase.invoke()
                }
                postCards.value = posts.map { PostCard(post = it) }
                supervisorScope {
                    postCards.value.forEach { postCard ->
                        launch {
                            loadDetails(
                                postCard.post,
                                comments.filter { it.postId == postCard.post.id })
                        }
                    }
                }
            } finally {
                isRefreshing.value = false
            }
        }
    }

    suspend fun loadDetails(post: PostItem, comments: List<CommentItem>){
        supervisorScope {
            val profPicUrl = async {
                try {
                    delay((1500L..4500L).random())
                    post.avatarUrl.ifEmpty { null }
                } catch (e: Exception) {
                    null
                }
            }.await()
            val commentsDelayed = async {
                try {
                    delay((1000L..4500L).random())
                    comments
                } catch (e: Exception) {
                    emptyList()
                }
            }.await()
            postCards.update { list ->
                list.map {
                    if (it.post.id == post.id) {
                        it.copy(
                            isProfPicLoading = if (profPicUrl.isNullOrEmpty()) LoadStatus.Error else LoadStatus.Ready,
                            isCommentsLoading = if (commentsDelayed.isEmpty()) LoadStatus.Error else LoadStatus.Ready,
                            profPicUrl = profPicUrl,
                            comments = commentsDelayed
                            )
                    } else {
                        it
                    }
                }
            }
        }
    }
    companion object {
        fun factory(getCommentsUseCase: GetCommentsUseCase, getPostsUseCase: GetPostsUseCase) =
            viewModelFactory { initializer {
                SocialNetViewModel(getCommentsUseCase, getPostsUseCase)
            } }
    }
}