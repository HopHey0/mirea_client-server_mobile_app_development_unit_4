package com.example.pract_3_4

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pract_3_4.feature.repoSearcher.data.local.ReposJsonDataSource
import com.example.pract_3_4.feature.repoSearcher.data.repository.RepoRepositoryImpl
import com.example.pract_3_4.feature.repoSearcher.domain.usecase.GetReposUseCase
import com.example.pract_3_4.feature.repoSearcher.domain.usecase.SearchReposUseCase
import com.example.pract_3_4.feature.repoSearcher.presentation.viewModel.RepoListViewModel
import com.example.pract_3_4.feature.socialNetwork.data.local.SocialNetJsonDataSource
import com.example.pract_3_4.feature.socialNetwork.data.repository.SocialNetRepositoryImpl
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetCommentsUseCase
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetPostsUseCase
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.SocialNetViewModel
import com.example.pract_3_4.navigation.AppNavHost
import com.example.pract_3_4.ui.theme.Pract_3_4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pract_3_4Theme {
                val socialNetViewModel = initSocialScreen(this)
                val repoSearcherViewModel = initRepoScreen(this)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController: NavHostController = rememberNavController()
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        socialNetViewModel = socialNetViewModel,
                        repoListViewModel = repoSearcherViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun initRepoScreen(context: Context): RepoListViewModel {
    val dataSource =
        ReposJsonDataSource(
            context
        )
    val repository =
        RepoRepositoryImpl(
            dataSource
        )
    val getReposUseCase =
        GetReposUseCase(
            repository
        )
    val searchReposUseCase =
        SearchReposUseCase(
            repository
        )
    val viewModel = viewModel<RepoListViewModel>(factory = RepoListViewModel.factory(getReposUseCase, searchReposUseCase))
    return viewModel
}

@Composable
fun initSocialScreen(context: Context): SocialNetViewModel{
    val dataSource = SocialNetJsonDataSource(context)

    val repository = SocialNetRepositoryImpl(dataSource)

    val getPostsUseCase = GetPostsUseCase(repository)

    val getCommentsUseCase = GetCommentsUseCase(repository)

    val viewModel = viewModel<SocialNetViewModel>(factory = SocialNetViewModel.factory(getCommentsUseCase, getPostsUseCase))

    return viewModel
}