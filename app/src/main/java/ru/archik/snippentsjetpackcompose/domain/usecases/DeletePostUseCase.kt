package ru.archik.snippentsjetpackcompose.domain.usecases

import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class DeletePostUseCase(
  private val repository: NewsFeedRepository
) {

  suspend operator fun invoke(feedPost: FeedPost) {
    repository.deletePost(feedPost)
  }
}