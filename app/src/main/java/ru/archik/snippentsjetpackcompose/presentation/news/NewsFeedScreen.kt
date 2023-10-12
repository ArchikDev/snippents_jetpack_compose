package ru.archik.snippentsjetpackcompose.presentation.news

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.archik.snippentsjetpackcompose.domain.entity.FeedPost
import ru.archik.snippentsjetpackcompose.ui.theme.DarkBlue

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NewsFeedScreen(
  paddingValues: PaddingValues,
  onCommentClickListener: (FeedPost) -> Unit
) {
  val viewModel: NewsFeedViewModel = viewModel()
  val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)

  when (val currentState = screenState.value) {
    is NewsFeedScreenState.Posts -> {
      FeedPosts(
        viewModel = viewModel,
        paddingValues = paddingValues,
        posts = currentState.posts,
        onCommentClickListener = onCommentClickListener,
        nextDataIsLoading = currentState.nextDataIsLoading
      )
    }
    NewsFeedScreenState.Loading -> {
      Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(color = DarkBlue)
      }
    }
    NewsFeedScreenState.Initial -> {}
    else -> {}
  }
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
  viewModel: NewsFeedViewModel,
  paddingValues: PaddingValues,
  posts: List<FeedPost>,
  onCommentClickListener: (FeedPost) -> Unit,
  nextDataIsLoading: Boolean
) {
  LazyColumn(
    modifier = Modifier.padding(paddingValues),
    contentPadding = PaddingValues(
      top = 16.dp,
      start = 8.dp,
      end = 8.dp,
      bottom = 16.dp
    ),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
      items(
        items = posts,
        key = { it.id }
      ) { feedPost ->
        val dismissState = rememberDismissState()
        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
          viewModel.remove(feedPost)
        }

        SwipeToDismiss(
          modifier = Modifier.animateItemPlacement(),
          state = dismissState,
          background = {},
          directions = setOf(DismissDirection.EndToStart)
        ) {
          PostCard(
            feedPost = feedPost,
            onCommentClickListener = {
              onCommentClickListener(feedPost)
            },
            onLikeClickListener = { _ ->
              viewModel.changeLikeStatus(feedPost)
            },
          )
        }
    }
    item {
      if (nextDataIsLoading) {
        Box(modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator(color = DarkBlue)
        }
      } else {
        // В контексте compose обычные функции вызывать нельзя, нужно через SideEffect или LaunchEffect
        SideEffect {
          viewModel.loadNextRecommendations()
        }

      }
    }
  }
}