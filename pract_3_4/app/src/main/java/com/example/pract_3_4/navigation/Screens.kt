package com.example.pract_3_4.navigation

sealed class Screens(val route: String){
    object RepoSearcher : Screens("repo")

    object SocialNetwork: Screens("socialNet")
}
