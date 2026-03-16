package com.example.pract_3_4.feature.socialNetwork.data.repository

import com.example.pract_3_4.feature.socialNetwork.data.local.SocialNetJsonDataSource
import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem
import com.example.pract_3_4.feature.socialNetwork.domain.repository.SocialNetRepository

class SocialNetRepositoryImpl(
    private val socialNetJsonDataSource: SocialNetJsonDataSource
) : SocialNetRepository {
    override fun getPosts(): List<PostItem> {
        return socialNetJsonDataSource.getPosts().map {
            postEntity -> PostItem(postEntity.id, postEntity.userId, postEntity.title, postEntity.body, postEntity.avatarUrl) }
        }

    override fun getComments(): List<CommentItem> {
        return socialNetJsonDataSource.getComments().map {
            commentEntity -> CommentItem(commentEntity.id, commentEntity.postId, commentEntity.name, commentEntity.body) }
        }
}