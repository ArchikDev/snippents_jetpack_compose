package ru.archik.snippentsjetpackcompose.presentation.comments

import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment

sealed class CommentsScreenState {
  object Initial: CommentsScreenState()

  data class Comments(
    val feedPost: FeedPost,
    val comments: List<PostComment>,
  ): CommentsScreenState()
}
