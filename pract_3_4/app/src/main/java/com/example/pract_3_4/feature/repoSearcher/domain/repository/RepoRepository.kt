package com.example.pract_3_4.feature.repoSearcher.domain.repository

import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun getRepos(): Flow<List<RepoItem>>

    fun searchRepos(query: String): Flow<List<RepoItem>>
}