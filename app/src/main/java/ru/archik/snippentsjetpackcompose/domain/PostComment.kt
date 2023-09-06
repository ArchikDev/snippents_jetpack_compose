package ru.archik.snippentsjetpackcompose.domain

import ru.archik.snippentsjetpackcompose.R

data class PostComment(
  val id: Int,
  val authorName: String = "Author",
  val authorAvatar: Int = R.drawable.comment_author_avatar,
  val commentText: String = "Большой текст комментария",
  val publicDate: String = "14:00"
)