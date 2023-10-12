package ru.archik.snippentsjetpackcompose.domain.usecases

import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository

class CheckAuthStateUseCase(
  private val repository: NewsFeedRepository
) {

  suspend operator fun invoke() {
    repository.checkAuthState()
  }
}