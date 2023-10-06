package ru.archik.snippentsjetpackcompose.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.archik.snippentsjetpackcompose.navigation.AppNavGraph
import ru.archik.snippentsjetpackcompose.navigation.rememberNavigationState
import ru.archik.snippentsjetpackcompose.presentation.comments.CommentsScreen
import ru.archik.snippentsjetpackcompose.presentation.news.NewsFeedScreen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainScreen() {
  // Выносим всю работу с навигацией в remeberNavigtationState
  val navigationState = rememberNavigationState()

  Scaffold(
    bottomBar = {
      BottomNavigation {
        val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
        val currentRout = navBackStackEntry?.destination?.route

        val items = listOf(
          NavigationItem.Home,
          NavigationItem.Favourite,
          NavigationItem.Profile
        )
        items.forEach { item ->
          BottomNavigationItem(
            selected = currentRout == item.screen.route,
            onClick = {
              navigationState.navigateTo(item.screen.route)
            },
            icon = {
              Icon(item.icon, contentDescription = null)
            },
            label = {
              Text(text = stringResource(id = item.titleResId))
            },
            selectedContentColor = MaterialTheme.colors.onPrimary,
            unselectedContentColor = MaterialTheme.colors.onSecondary
          )
        }
      }
    }
  ) { paddingValues ->
    AppNavGraph(
      navHostController = navigationState.navHostController,
      newsFeedScreenContent = {
        NewsFeedScreen(
          paddingValues = paddingValues,
          onCommentClickListener = {
            navigationState.navigateToComments(it)
          }
        )
      },
      commentsScreenContent = { feedPost ->
        CommentsScreen(
          onBackPressed = {
            navigationState.navHostController.popBackStack()
          },
          feedPost = feedPost
        )
      },
      favouriteScreenContent = { TextCounter(name = "Favourite") },
      profileScreenContent = { TextCounter(name = "Profile") }
    )
  }
}

@Composable
private fun TextCounter(name: String) {
  // remember - не сохраняется при рекомпозиции
  // rememberSaveable - сохраняется при рекомпозиции(например переворот экрана)
  var count by rememberSaveable {
    mutableStateOf(0)
  }

  Text(
    modifier = Modifier.clickable { count++ },
    text = "$name Count: $count",
    color = Color.Black
  )
}