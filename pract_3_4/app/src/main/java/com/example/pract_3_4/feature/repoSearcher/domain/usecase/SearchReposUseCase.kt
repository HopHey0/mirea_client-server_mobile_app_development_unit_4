package com.example.pract_3_4.feature.repoSearcher.domain.usecase

import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.feature.repoSearcher.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow

class SearchReposUseCase(private val repository: RepoRepository) {
    fun invoke(query: String): Flow<List<RepoItem>> = repository.searchRepos(query)
}