package ru.archik.snippentsjetpackcompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment
import ru.archik.snippentsjetpackcompose.domain.StatisticItem

class MainViewModel : ViewModel() {

  private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
  val authState: LiveData<AuthState> = _authState

  init {
    _authState.value = if (VK.isLoggedIn()) AuthState.Authorized else AuthState.NotAuthorized
  }

  fun performAuthResult(result: VKAuthenticationResult) {
    if (result is VKAuthenticationResult.Success) {
      _authState.value = AuthState.Authorized
    } else {
      // (result as VKAuthenticationResult.Failed).exception - обьект ошибки
      _authState.value = AuthState.NotAuthorized
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