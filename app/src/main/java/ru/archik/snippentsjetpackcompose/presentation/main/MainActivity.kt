package ru.archik.snippentsjetpackcompose.presentation.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import ru.archik.snippentsjetpackcompose.ui.theme.SnippentsJetpackComposeTheme

class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      SnippentsJetpackComposeTheme {

        val viewModel: MainViewModel = viewModel()
        val authState = viewModel.authState.observeAsState(AuthState.Initial)

        val launcher = rememberLauncherForActivityResult(
          contract = VK.getVKAuthActivityResultContract(),
        ) {
          viewModel.performAuthResult(it)
        }

        when (authState.value) {
          is AuthState.Authorized -> {
            MainScreen()
          }
          is AuthState.NotAuthorized -> {
            LoginScreen {
              launcher.launch(listOf(VKScope.WALL))
            }
          }
          else -> {}
        }
      }
    }
  }
}

