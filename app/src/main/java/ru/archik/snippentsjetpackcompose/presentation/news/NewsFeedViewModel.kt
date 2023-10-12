package ru.archik.snippentsjetpackcompose.presentation.news

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepository
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.StatisticItem
import ru.archik.snippentsjetpackcompose.extensions.mergeWith

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = NewsFeedRepository(application)

  private val recommendationsFlow = repository.recommendations

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
      repository.loadNextData()
    }
  }

  @RequiresApi(Build.VERSION_CODES.N)
  fun changeLikeStatus(feedPost: FeedPost) {
    viewModelScope.launch {
      repository.changeLikeStatus(feedPost)
    }
  }

  fun remove(feedPost: FeedPost) {
    viewModelScope.launch {
      repository.deletePost(feedPost)
    }

  }
}