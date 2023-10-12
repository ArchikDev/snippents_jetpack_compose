package ru.archik.snippentsjetpackcompose.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.archik.snippentsjetpackcompose.domain.entity.AuthState
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class GetAuthStateFlowUseCase(
  private val repository: NewsFeedRepository
) {

  operator fun invoke(): StateFlow<AuthState> {
    return repository.getAuthStateFlow()
  }
}