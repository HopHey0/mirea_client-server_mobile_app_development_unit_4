package com.example.pract_3_4.feature.repoSearcher.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.feature.repoSearcher.domain.usecase.GetReposUseCase
import com.example.pract_3_4.feature.repoSearcher.domain.usecase.SearchReposUseCase
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetCommentsUseCase
import com.example.pract_3_4.feature.socialNetwork.domain.usecase.GetPostsUseCase
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.SocialNetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoListViewModel(
    private val getReposUseCase: GetReposUseCase,
    private val searchReposUseCase: SearchReposUseCase
) : ViewModel() {
    val repos = MutableStateFlow<List<RepoItem>>(emptyList())
    val searchRes = repos

    val isLoading = MutableStateFlow(true)

    var searchJob: Job? = null

    init {
        findAllRepos()
    }

    fun findAllRepos(){
        viewModelScope.launch {
            isLoading.value = true
            repos.value = getReposUseCase.invoke().first()
            delay(1000)
            isLoading.value = false
        }
    }
    fun searchRepos(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            findAllRepos()
            isLoading.value = false
            return
        }
        searchJob = viewModelScope.launch {
            isLoading.value = true

            delay(500)

            val results = withContext(Dispatchers.IO) {
                searchReposUseCase.invoke(query).first()
            }

            repos.value = results
            isLoading.value = false
        }
    }
    companion object {
        fun factory(getReposUseCase: GetReposUseCase, searchReposUseCase: SearchReposUseCase) =
            viewModelFactory { initializer {
                RepoListViewModel(getReposUseCase, searchReposUseCase)
            } }
    }
}