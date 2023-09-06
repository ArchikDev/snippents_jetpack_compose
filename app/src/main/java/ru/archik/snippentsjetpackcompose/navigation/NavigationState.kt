package ru.archik.snippentsjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(
  val navHostController: NavHostController
) {
  fun navigateTo(route: String) {
    navHostController.navigate(route) {
      launchSingleTop = true // не пересоздается экран если он текущий
      // из бэкстэка удаляем все экраны до стартого экрана
      // navHostController.graph.startDestinationId - получаем первый экран
      popUpTo(navHostController.graph.startDestinationId) {
        // state сохраняется когда экраны удаляются из бэкстэка
        saveState = true
      } // история переходов не хранится в приложении
      restoreState = true // восстановление state после удаления экрана
    }
  }
}

@Composable
fun remeberNavigtationState(
  navHostController: NavHostController = rememberNavController()
):NavigationState {
  return remember {
    NavigationState(navHostController)
  }
}