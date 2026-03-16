package com.example.pract_3_4.feature.socialNetwork.data.local

import android.content.Context
import com.example.pract_3_4.R
import com.example.pract_3_4.feature.socialNetwork.data.local.entity.CommentEntity
import com.example.pract_3_4.feature.socialNetwork.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.json.Json

class SocialNetJsonDataSource(private val context: Context) {
    fun getPosts(): List<PostEntity> {
        val postsFile = context.resources.openRawResource(R.raw.social_posts).bufferedReader().use {
            it.readText()
        }
        //Log.v("HERE", reposFile)
        val jsonPosts = Json.decodeFromString<List<PostEntity>>(postsFile)
        return jsonPosts
    }
    fun getComments(): List<CommentEntity> {
        val commentsFile = context.resources.openRawResource(R.raw.comments).bufferedReader().use {
            it.readText()
        }
        //Log.v("HERE", reposFile)
        val jsonComments = Json.decodeFromString<List<CommentEntity>>(commentsFile)
        return jsonComments
    }
}