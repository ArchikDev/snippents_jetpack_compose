package ru.archik.snippentsjetpackcompose.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.archik.snippentsjetpackcompose.domain.entity.AuthState
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.entity.PostComment

interface NewsFeedRepository {

  fun getAuthStateFlow(): StateFlow<AuthState>

  fun getRecommendations(): StateFlow<List<FeedPost>>

  fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

  suspend fun loadNextData()

  suspend fun checkAuthState()

  suspend fun deletePost(feedPost: FeedPost)

  suspend fun changeLikeStatus(feedPost: FeedPost)
}