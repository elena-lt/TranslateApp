package com.lutty.translate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lutty.translate.TranslateApp
import com.lutty.translate.android.core.Routes
import com.lutty.translate.android.core.Routes.TRANSLATE
import com.lutty.translate.android.translate.presentation.AndroidTranslateViewModel
import com.lutty.translate.android.translate.presentation.TranslateScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TranslateTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          TranslateRoot()
        }
      }
    }
  }
}

@Composable
fun TranslateRoot() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = Routes.TRANSLATE
  ) {
    composable(route = TRANSLATE) {
      val viewModel = hiltViewModel<AndroidTranslateViewModel>()
      val state by viewModel.state.collectAsState()
      TranslateScreen(state = state, onEvent = viewModel::onEvent)
    }
  }
}

@Preview
@Composable
fun DefaultPreview() {
  TranslateTheme {
    TranslateApp()
  }
}
