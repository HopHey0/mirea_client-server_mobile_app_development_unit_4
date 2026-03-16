package com.example.pract_3_4.feature.repoSearcher.domain.usecase

import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.feature.repoSearcher.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow

class GetReposUseCase(private val repository: RepoRepository) {
    fun invoke(): Flow<List<RepoItem>> = repository.getRepos()
}
