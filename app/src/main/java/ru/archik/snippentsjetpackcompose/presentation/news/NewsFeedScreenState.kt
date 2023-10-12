package ru.archik.snippentsjetpackcompose.presentation.news

import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost

sealed class NewsFeedScreenState {

  object Initial: NewsFeedScreenState() // хорошая практика, чтобы обьект был не нулабельный

  object Loading: NewsFeedScreenState()

  data class Posts(
    val posts: List<FeedPost>,
    val nextDataIsLoading: Boolean = false,
  ): NewsFeedScreenState()

}