package ru.archik.snippentsjetpackcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import ru.archik.snippentsjetpackcompose.ui.theme.SnippentsJetpackComposeTheme

class MainActivity : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SnippentsJetpackComposeTheme {
        MainScreen(viewModel)
      }
    }
  }
}

