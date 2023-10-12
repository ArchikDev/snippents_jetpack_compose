package ru.archik.snippentsjetpackcompose.presentation.comments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepository
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment

class CommentsViewModel(
  feedPost: FeedPost,
  application: Application
) : ViewModel() {

  private val repository = NewsFeedRepository(application)

  val screenState = repository.getComments(feedPost)
    .map { CommentsScreenState.Comments(
      feedPost = feedPost,
      comments = it
    ) }

}