package com.example.pract_3_4.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pract_3_4.feature.repoSearcher.presentation.ui.screen.RepoList
import com.example.pract_3_4.feature.repoSearcher.presentation.viewModel.RepoListViewModel
import com.example.pract_3_4.feature.socialNetwork.presentation.ui.screen.PostList
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.SocialNetViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    socialNetViewModel: SocialNetViewModel,
    repoListViewModel: RepoListViewModel,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Screens.RepoSearcher.route) {
        composable(Screens.RepoSearcher.route) {
            RepoList(
                repoListViewModel = repoListViewModel,
                onClickButton = {
                    navController.navigate(Screens.SocialNetwork.route)
                },
                modifier
            )
        }
        composable(Screens.SocialNetwork.route) {
            PostList(
                socialNetViewModel = socialNetViewModel,
                modifier
            )
        }
    }
}