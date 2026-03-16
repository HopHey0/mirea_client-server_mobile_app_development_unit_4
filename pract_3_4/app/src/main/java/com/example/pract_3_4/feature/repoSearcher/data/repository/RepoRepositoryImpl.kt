package com.example.pract_3_4.feature.repoSearcher.data.repository

import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.feature.repoSearcher.data.local.ReposJsonDataSource
import com.example.pract_3_4.feature.repoSearcher.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepoRepositoryImpl(
    private val reposJsonDataSource: ReposJsonDataSource
) : RepoRepository {
    override fun getRepos(): Flow<List<RepoItem>> {
        return reposJsonDataSource.getRepos().map { list ->
             list.map { repo -> RepoItem(repo.id, repo.fullName, repo.description, repo.starsCount, repo.language) }
        }
    }

    override fun searchRepos(query: String): Flow<List<RepoItem>> {
        return getRepos().map { list ->
            list.filter { repo ->
                repo.fullName.contains(query, ignoreCase = true)
            }
        }
    }
}