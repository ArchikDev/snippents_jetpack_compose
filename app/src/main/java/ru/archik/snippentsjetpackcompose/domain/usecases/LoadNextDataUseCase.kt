package ru.archik.snippentsjetpackcompose.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class LoadNextDataUseCase(
  private val repository: NewsFeedRepository
) {

  suspend operator fun invoke() {
    repository.loadNextData()
  }
}