package ru.archik.snippentsjetpackcompose

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import ru.archik.snippentsjetpackcompose.ui.theme.SnippentsJetpackComposeTheme

class MainActivity : ComponentActivity() {
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

