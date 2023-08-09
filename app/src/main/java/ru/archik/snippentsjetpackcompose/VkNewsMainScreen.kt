package ru.archik.snippentsjetpackcompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.archik.snippentsjetpackcompose.navigation.AppNavGraph

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
            onClick = { navHostController.navigate(item.screen.route) },
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
  var count by remember {
    mutableStateOf(0)
  }

  Text(
    modifier = Modifier.clickable { count++ },
    text = "$name Count: $count",
    color = Color.Black
  )
}