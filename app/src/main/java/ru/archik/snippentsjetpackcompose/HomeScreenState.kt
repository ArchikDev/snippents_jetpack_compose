package ru.archik.snippentsjetpackcompose

import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment

sealed class HomeScreenState {

  object Initial: HomeScreenState() // хорошая практика, чтобы обьект был не нулабельный

  data class Posts(val posts: List<FeedPost>): HomeScreenState()
  data class Comments(val feedPost: FeedPost, val comments: List<PostComment>): HomeScreenState()
}