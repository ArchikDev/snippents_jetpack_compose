package ru.archik.snippentsjetpackcompose

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.archik.snippentsjetpackcompose.domain.FeedPost

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
  viewModel: MainViewModel,
  paddingValues: PaddingValues,
) {
  val screenState = viewModel.screenState.observeAsState(HomeScreenState.Initial)

  when (val currentState = screenState.value) {
    is HomeScreenState.Posts -> {
      FeedPosts(
        posts = currentState.posts,
        viewModel = viewModel,
        paddingValues = paddingValues
      )
    }
    is HomeScreenState.Comments -> {
      CommentsScreen(
        feedPost = currentState.feedPost,
        comments = currentState.comments,
        onBackPressed = {
          viewModel.closeComments()
        }
      )
      BackHandler { // Кнопка назад на устройстве
        viewModel.closeComments()
      }
    }
    is HomeScreenState.Initial -> {}
  }

}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
  posts: List<FeedPost>,
  viewModel: MainViewModel,
  paddingValues: PaddingValues
) {
  LazyColumn(
    modifier = Modifier.padding(paddingValues),
    contentPadding = PaddingValues(
      top = 16.dp,
      start = 8.dp,
      end = 8.dp,
      bottom = 72.dp
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
          onViewsClickListener = { statisticItem ->
            viewModel.updateCount(feedPost, statisticItem)
          },
          onShareClickListener = { statisticItem ->
            viewModel.updateCount(feedPost, statisticItem)
          },
          onCommentClickListener = {
            viewModel.showComments(feedPost)
          },
          onLikeClickListener = { statisticItem ->
            viewModel.updateCount(feedPost, statisticItem)
          },
        )
      }
    }
  }
}