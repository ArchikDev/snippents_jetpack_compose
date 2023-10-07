package ru.archik.snippentsjetpackcompose.presentation.news

import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment

sealed class NewsFeedScreenState {

  object Initial: NewsFeedScreenState() // хорошая практика, чтобы обьект был не нулабельный

  data class Posts(
    val posts: List<FeedPost>,
    val nextDataIsLoading: Boolean = false,
  ): NewsFeedScreenState()

}