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
  private val _feedPosts = MutableLiveData<List<FeedPost>>(sourceList)
  val feedPosts: LiveData<List<FeedPost>> = _feedPosts

  @RequiresApi(Build.VERSION_CODES.N)
  fun updateCount(feedPost: FeedPost, item: StatisticItem) {
    val oldPosts = feedPosts.value?.toMutableList() ?: mutableListOf()
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
    _feedPosts.value = oldPosts.apply {
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
    val oldPosts = feedPosts.value?.toMutableList() ?: mutableListOf()
    oldPosts.remove(feedPost)
    _feedPosts.value = oldPosts
  }
}