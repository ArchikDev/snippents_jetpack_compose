package ru.archik.snippentsjetpackcompose.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.archik.snippentsjetpackcompose.data.model.LikesCountResponseDto
import ru.archik.snippentsjetpackcompose.data.model.NewsFeedResponseDto

interface ApiService {

  @GET("newsfeed.getRecommended?v=5.131")
  suspend fun loadRecommendations(
    @Query("access_token") token: String
  ): NewsFeedResponseDto

  @GET("newsfeed.getRecommended?v=5.131")
  suspend fun loadRecommendations(
    @Query("access_token") token: String,
    @Query("start_from") startFrom: String
  ): NewsFeedResponseDto

  @GET("likes.add?v=5.131&type=post")
  suspend fun addLike(
    @Query("access_token") token: String,
    @Query("owner_id") ownerId: Long,
    @Query("item_id") postId: Long
  ): LikesCountResponseDto

  @GET("likes.delete?v=5.131&type=post")
  suspend fun deleteLike(
    @Query("access_token") token: String,
    @Query("owner_id") ownerId: Long,
    @Query("item_id") postId: Long
  ): LikesCountResponseDto

  @GET("newsfeed.ignoreItem?v=5.131&type=wall")
  suspend fun ignorePost(
    @Query("access_token") token: String,
    @Query("owner_id") ownerId: Long,
    @Query("item_id") postId: Long
  )

}