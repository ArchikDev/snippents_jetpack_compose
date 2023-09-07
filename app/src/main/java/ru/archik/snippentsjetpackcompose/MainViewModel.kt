package ru.archik.snippentsjetpackcompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.StatisticItem

class MainViewModel : ViewModel() {

  private val sourceList = mutableListOf<FeedPost>().apply {
    repeat(10) {
      add(FeedPost(id = it))
    }
  }

  private val initialState = HomeScreenState.Posts(posts = sourceList)

  private val _screenState = MutableLiveData<HomeScreenState>(initialState)
  val screenState: LiveData<HomeScreenState> = _screenState

  @RequiresApi(Build.VERSION_CODES.N)
  fun updateCount(feedPost: FeedPost, item: StatisticItem) {
    val oldPosts = screenState.value?.toMutableList() ?: mutableListOf()
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
    _screenState.value = oldPosts.apply {
      replaceAll {
        if (it.id == newFeedPost.id) {
          newFeedPost
        } else {
          it
        }
      }
    }
  }

  fun remove(feedPost: FeedPost) {
    val oldPosts = screenState.value?.toMutableList() ?: mutableListOf()
    oldPosts.remove(feedPost)
    _screenState.value = oldPosts
  }
}