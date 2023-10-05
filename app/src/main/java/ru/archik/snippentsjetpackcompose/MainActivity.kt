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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import ru.archik.snippentsjetpackcompose.ui.theme.SnippentsJetpackComposeTheme

class MainActivity : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

//    VK.login(activity, arrayListOf(VKScope.WALL, VKScope.PHOTOS))

    setContent {
      SnippentsJetpackComposeTheme {

        val launcher = rememberLauncherForActivityResult(
          contract = VK.getVKAuthActivityResultContract(),
        ) {
          when (it) {
            is VKAuthenticationResult.Success -> {
              Log.d("MainActivity", "success")
            }
            is VKAuthenticationResult.Failed -> {
              Log.d("MainActivity", "error")
            }
          }
        }

        launcher.launch(listOf(VKScope.WALL))

        MainScreen(viewModel)
      }
    }
  }
}

