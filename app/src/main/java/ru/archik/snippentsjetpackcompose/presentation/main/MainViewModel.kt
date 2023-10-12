package ru.archik.snippentsjetpackcompose.presentation.main

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = NewsFeedRepository(application)

  val authState =repository.authStateFlow

  fun performAuthResult() {
    viewModelScope.launch {
      repository.checkAuthState()
    }
  }





//  private val comments = mutableListOf<PostComment>().apply {
//    repeat(10) {
//      add(PostComment(id = it))
//    }
//  }
//
//  private val sourceList = mutableListOf<FeedPost>().apply {
//    repeat(10) {
//      add(FeedPost(id = it))
//    }
//  }
//
//  private val initialState = HomeScreenState.Posts(posts = sourceList)
//
//  private val _screenState = MutableLiveData<HomeScreenState>(initialState)
//  val screenState: LiveData<HomeScreenState> = _screenState
//
//  private var savedState: HomeScreenState? = initialState
//
//  fun showComments(feedPost: FeedPost) {
//    savedState = _screenState.value
//
//    _screenState.value = HomeScreenState.Comments(
//      feedPost = feedPost,
//      comments = comments
//    )
//  }
//
//  fun closeComments() {
//    _screenState.value = savedState
//  }
//
//  @RequiresApi(Build.VERSION_CODES.N)
//  fun updateCount(feedPost: FeedPost, item: StatisticItem) {
//    val currentState = screenState.value
//
//    if (currentState !is HomeScreenState.Posts) return
//
//    val oldPosts = currentState.posts.toMutableList()
//    val oldStatistics = feedPost.statistics
//    val newStatistics = oldStatistics.toMutableList().apply {
//      replaceAll { oldItem ->
//        if (oldItem.type == item.type) {
//          oldItem.copy(count = oldItem.count + 1)
//        } else {
//          oldItem
//        }
//      }
//    }
//    val newFeedPost = feedPost.copy(statistics = newStatistics)
//    val newPosts = oldPosts.apply {
//      replaceAll {
//        if (it.id == newFeedPost.id) {
//          newFeedPost
//        } else {
//          it
//        }
//      }
//    }
//
//    _screenState.value = HomeScreenState.Posts(posts = newPosts)
//  }
//
//  fun remove(feedPost: FeedPost) {
//    val currentState = screenState.value
//
//    if (currentState !is HomeScreenState.Posts) return
//
//    val oldPosts = currentState.posts.toMutableList()
//
//    oldPosts.remove(feedPost)
//
//    HomeScreenState.Posts(posts = oldPosts)
//  }
}