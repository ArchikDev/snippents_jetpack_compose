package ru.archik.snippentsjetpackcompose.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepositoryImpl
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.domain.usecases.GetCommentsUseCase
import ru.archik.snippentsjetpackcompose.domain.usecases.GetRecommendationsUseCase

class CommentsViewModel(
  feedPost: FeedPost,
  application: Application
) : ViewModel() {

  private val repository = NewsFeedRepositoryImpl(application)

  private val getCommentsUseCase = GetCommentsUseCase(repository)

  val screenState = getCommentsUseCase(feedPost)
    .map { CommentsScreenState.Comments(
      feedPost = feedPost,
      comments = it
    ) }

}