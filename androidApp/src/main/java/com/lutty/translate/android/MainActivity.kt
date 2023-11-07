package com.lutty.translate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lutty.translate.TranslateApp
import com.lutty.translate.android.core.Routes
import com.lutty.translate.android.core.Routes.TRANSLATE
import com.lutty.translate.android.core.Routes.VOICE_TO_TEXT
import com.lutty.translate.android.translate.presentation.AndroidTranslateViewModel
import com.lutty.translate.android.translate.presentation.TranslateScreen
import com.lutty.translate.android.voiceToText.presentation.AndroidVoiceToTextViewModel
import com.lutty.translate.android.voiceToText.presentation.VoiceToTextScreen
import com.lutty.translate.core.presentation.TranslateEvent.RecordAudio
import com.lutty.translate.core.presentation.TranslateEvent.SubmitVoiceResult
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent.Close
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

const val LANGUAGE_CODE_ARG = "languageCode"
const val VOICE_RESULT_ARG = "voiceResult"

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
      val voiceResult by it.savedStateHandle
        .getStateFlow<String?>(VOICE_RESULT_ARG, null)
        .collectAsState()

      LaunchedEffect(key1 = voiceResult) {
        viewModel.onEvent(SubmitVoiceResult(voiceResult))
        it.savedStateHandle[VOICE_RESULT_ARG] = null
      }

      TranslateScreen(
        state = state,
        onEvent = { event ->
          if (event is RecordAudio) {
            navController.navigate(VOICE_TO_TEXT + "/${state.fromLanguage.language.langCode}")
          } else viewModel.onEvent(event)
        }
      )
    }

    composable(
      route = "$VOICE_TO_TEXT/{$LANGUAGE_CODE_ARG}",
      arguments = listOf(
        navArgument(LANGUAGE_CODE_ARG) {
          type = NavType.StringType
          defaultValue = "en"
        }
      )
    ) { backStackEntry ->
      val langCode = backStackEntry.arguments?.getString(LANGUAGE_CODE_ARG) ?: "en"
      val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
      val state by viewModel.state.collectAsState()

      VoiceToTextScreen(
        state = state,
        langCode = langCode,
        onResult = { spokenText ->
          navController.previousBackStackEntry?.savedStateHandle?.set(VOICE_RESULT_ARG, spokenText)
          navController.popBackStack()
        },
        onEvent = { event ->
          when (event) {
            Close -> navController.popBackStack()
            else -> viewModel.onEvent(event)
          }

        }
      )
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
