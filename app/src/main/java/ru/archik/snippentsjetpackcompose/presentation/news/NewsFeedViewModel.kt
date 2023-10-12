package ru.archik.snippentsjetpackcompose.presentation.news

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepositoryImpl
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.usecases.ChangeLikeStateUseCase
import ru.archik.snippentsjetpackcompose.domain.usecases.DeletePostUseCase
import ru.archik.snippentsjetpackcompose.domain.usecases.GetRecommendationsUseCase
import ru.archik.snippentsjetpackcompose.domain.usecases.LoadNextDataUseCase
import ru.archik.snippentsjetpackcompose.extensions.mergeWith

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

  private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    Log.d("vmNewsFeed", "Error")
  }

  private val repository = NewsFeedRepositoryImpl(application)

  private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
  private val loadNextDataUseCase = LoadNextDataUseCase(repository)
  private val changeLikeStatusUseCase = ChangeLikeStateUseCase(repository)
  private val deletePostUseCase = DeletePostUseCase(repository)

  private val recommendationsFlow = getRecommendationsUseCase()

  private val loadNextDataEvents = MutableSharedFlow<Unit>()
  private val loadNextDataFlow = flow {
    loadNextDataEvents.collect {
      emit(
        NewsFeedScreenState.Posts(
          posts = recommendationsFlow.value,
          nextDataIsLoading = true
        )
      )
    }
  }

  val screenState = recommendationsFlow
    .filter { it.isNotEmpty() }
    .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
    .onStart { emit(NewsFeedScreenState.Loading) }
    .mergeWith(loadNextDataFlow)

  fun loadNextRecommendations() {
    viewModelScope.launch {
      loadNextDataEvents.emit(Unit)
      loadNextDataUseCase()
    }
  }

  @RequiresApi(Build.VERSION_CODES.N)
  fun changeLikeStatus(feedPost: FeedPost) {
    viewModelScope.launch(exceptionHandler) {
      changeLikeStatusUseCase(feedPost)
    }
  }

  fun remove(feedPost: FeedPost) {
    viewModelScope.launch(exceptionHandler) {
      deletePostUseCase(feedPost)
    }

  }
}