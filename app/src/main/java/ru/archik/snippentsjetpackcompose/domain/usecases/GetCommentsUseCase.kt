package ru.archik.snippentsjetpackcompose.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.entity.PostComment
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class GetCommentsUseCase(
  private val repository: NewsFeedRepository
) {

  operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>> {
    return repository.getComments(feedPost)
  }
}