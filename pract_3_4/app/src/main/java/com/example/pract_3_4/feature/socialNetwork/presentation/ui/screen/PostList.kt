package com.example.pract_3_4.feature.socialNetwork.presentation.ui.screen

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pract_3_4.feature.socialNetwork.data.local.SocialNetJsonDataSource
import com.example.pract_3_4.feature.socialNetwork.data.repository.SocialNetRepositoryImpl
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetCommentsUseCase
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetPostsUseCase
import com.example.pract_3_4.feature.socialNetwork.presentation.ui.component.Post
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.SocialNetViewModel
import com.example.pract_3_4.ui.theme.Pract_3_4Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostList(
    socialNetViewModel: SocialNetViewModel,
    modifier: Modifier = Modifier
){
    val posts = socialNetViewModel.postCards.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (posts.value.isEmpty()) {
            socialNetViewModel.loadPosts()
        }
    }
    val isRefreshing = socialNetViewModel.isRefreshing.collectAsStateWithLifecycle()
    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = { socialNetViewModel.refreshPosts() }
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(posts.value) { item ->
                Post(item)
            }
        }
    }
}

fun createViewModelForPreview(context: Context): SocialNetViewModel {
    return SocialNetViewModel(GetCommentsUseCase(SocialNetRepositoryImpl(
        SocialNetJsonDataSource(context))),
        GetPostsUseCase(SocialNetRepositoryImpl(SocialNetJsonDataSource(context))))
}
@Composable
@Preview(showSystemUi = true)
fun PostListPreview(){
    Pract_3_4Theme {
        PostList(createViewModelForPreview(LocalContext.current))
    }
}
