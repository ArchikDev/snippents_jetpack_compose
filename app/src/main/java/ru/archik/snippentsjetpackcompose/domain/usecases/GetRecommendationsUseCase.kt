package ru.archik.snippentsjetpackcompose.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class GetRecommendationsUseCase(
  private val repository: NewsFeedRepository
) {

  operator fun invoke(): StateFlow<List<FeedPost>> {
    return repository.getRecommendations()
  }
}