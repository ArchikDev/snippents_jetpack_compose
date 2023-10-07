package ru.archik.snippentsjetpackcompose.presentation.news

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepository
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.StatisticItem

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

  private val initialState = NewsFeedScreenState.Initial

  private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
  val screenState: LiveData<NewsFeedScreenState> = _screenState

  private val repository = NewsFeedRepository(application)

  init {
    _screenState.value = NewsFeedScreenState.Loading

    loadRecommendations()
  }

  private fun loadRecommendations() {
    viewModelScope.launch {
      val feedPost = repository.loadRecommendations()

      _screenState.value = NewsFeedScreenState.Posts(posts = feedPost)
    }
  }

  fun loadNextRecommendations() {
    _screenState.value = NewsFeedScreenState.Posts(
      posts = repository.feedPosts,
      nextDataIsLoading = true
    )

    loadRecommendations()
  }

  @RequiresApi(Build.VERSION_CODES.N)
  fun changeLikeStatus(feedPost: FeedPost) {
    viewModelScope.launch {
      repository.changeLikeStatus(feedPost)

      _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
    }
  }

  fun remove(feedPost: FeedPost) {
    viewModelScope.launch {
      repository.deletePost(feedPost)

      _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
    }

  }
}