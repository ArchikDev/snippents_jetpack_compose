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


  @RequiresApi(Build.VERSION_CODES.N)
  fun updateCount(feedPost: FeedPost, item: StatisticItem) {
    val currentState = screenState.value
    if (currentState !is NewsFeedScreenState.Posts) return

    val oldPosts = currentState.posts.toMutableList()
    val oldStatistics = feedPost.statistics
    val newStatistics = oldStatistics.toMutableList().apply {
      replaceAll { oldItem ->
        if (oldItem.type == item.type) {
          oldItem.copy(count = oldItem.count + 1)
        } else {
          oldItem
        }
      }
    }
    val newFeedPost = feedPost.copy(statistics = newStatistics)
    val newPosts = oldPosts.apply {
      replaceAll {
        if (it.id == newFeedPost.id) {
          newFeedPost
        } else {
          it
        }
      }
    }
    _screenState.value = NewsFeedScreenState.Posts(posts = newPosts)
  }

  fun remove(feedPost: FeedPost) {
    val currentState = screenState.value
    if (currentState !is NewsFeedScreenState.Posts) return

    val oldPosts = currentState.posts.toMutableList()
    oldPosts.remove(feedPost)
    _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)
  }
}