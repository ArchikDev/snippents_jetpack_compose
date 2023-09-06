package ru.archik.snippentsjetpackcompose

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
import androidx.navigation.compose.rememberNavController
import ru.archik.snippentsjetpackcompose.navigation.AppNavGraph
import ru.archik.snippentsjetpackcompose.navigation.Screen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainScreen(viewModel: MainViewModel) {
  val navHostController = rememberNavController()

  Scaffold(
    bottomBar = {
      BottomNavigation {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
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
              navHostController.navigate(item.screen.route) {
                launchSingleTop = true // не пересоздается экран если он текущий
                // из бэкстэка удаляем все экраны до стартого экрана
                popUpTo(Screen.NewsFeed.route) {
                  // state сохраняется когда экраны удаляются из бэкстэка
                  saveState = true
                } // история переходов не хранится в приложении
                restoreState = true // восстановление state после удаления экрана
              }
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
      navHostController = navHostController,
      homeScreenContent = {
        HomeScreen(
          viewModel = viewModel,
          paddingValues = paddingValues
        )
      },
      favoriteScreenContent = { TextCounter(name = "Favourite") },
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