package ru.archik.snippentsjetpackcompose.domain

sealed class NewsFeedResult {
  object Error : NewsFeedResult()

  data class Success(val posts: List<FeedPost>) : NewsFeedResult()
}