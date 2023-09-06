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
import ru.archik.snippentsjetpackcompose.navigation.NavigationState
import ru.archik.snippentsjetpackcompose.navigation.Screen
import ru.archik.snippentsjetpackcompose.navigation.remeberNavigtationState

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainScreen(viewModel: MainViewModel) {
//  val navController = rememberNavController()
//  val navigationState = remember {
//    // Мы не можем сразу вызвать rememberNavController(), т.к.
//    // remember принимает лямбду - функцию, которая не является composable - функцией
//    // rememberNavController() - может вызываться только в composable - функции
//    NavigationState(navController)
//  }

  // Выносим всю работу с навигацией в remeberNavigtationState
  val navigationState = remeberNavigtationState()

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