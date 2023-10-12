package ru.archik.snippentsjetpackcompose.presentation.comments

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.archik.snippentsjetpackcompose.R
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment

@Composable
fun CommentsScreen(
  feedPost: FeedPost,
  onBackPressed: () -> Unit
) {
  val viewModel: CommentsViewModel = viewModel(
    factory = CommentsViewModelFactory(
      feedPost,
      LocalContext.current.applicationContext as Application // Так получаем Application внутри compose функции
    )
  )

  val screenState = viewModel.screenState.collectAsState(CommentsScreenState.Initial)
  val currentState = screenState.value

  if (currentState is CommentsScreenState.Comments) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(text = stringResource(R.string.comments_title))
          },
          navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
              Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
              )
            }
          }
        )
      }
    ) { paddingValues ->
      LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
          top = 16.dp,
          start = 8.dp,
          end = 8.dp,
          bottom = 72.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(
          items = currentState.comments,
          key = { it.id }
        ) { comment ->
          CommentItem(comment = comment)
        }
      }
    }
  }

}

@Composable
private fun CommentItem(
  comment: PostComment
) {
  Row(modifier = Modifier
    .fillMaxWidth()
    .padding(
      horizontal = 16.dp,
      vertical = 4.dp
    )
  ) {
    AsyncImage(
      model = comment.authorAvatarUrl,
      modifier = Modifier.size(48.dp).clip(CircleShape),
      contentDescription = null
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(
        text = comment.authorName,
        color = MaterialTheme.colors.onPrimary,
        fontSize = 12.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = comment.commentText,
        color = MaterialTheme.colors.onPrimary,
        fontSize = 14.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = comment.publicDate,
        color = MaterialTheme.colors.onSecondary,
        fontSize = 12.sp
      )
    }
  }
}