package com.example.pract_3_4.feature.repoSearcher.data.local

import android.content.Context
import android.util.Log
import com.example.pract_3_4.R
import com.example.pract_3_4.feature.repoSearcher.data.local.entity.RepoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.json.Json

class ReposJsonDataSource(private val context: Context) {
    fun getRepos(): Flow<List<RepoEntity>> {
        val reposFile = context.resources.openRawResource(R.raw.repositories).bufferedReader().use {
            it.readText()
        }
        //Log.v("HERE", reposFile)
        val jsonRepos = Json.decodeFromString<List<RepoEntity>>(reposFile)
        return flowOf(jsonRepos)
    }
}