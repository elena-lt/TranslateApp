package com.lutty.translate.android.voiceToText.presentation

import android.Manifest.permission.RECORD_AUDIO
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lutty.translate.android.R
import com.lutty.translate.android.R.string
import com.lutty.translate.android.core.theme.LightBlue
import com.lutty.translate.voiceToText.presentation.DisplayState.DISPLAYING_RESULTS
import com.lutty.translate.voiceToText.presentation.DisplayState.ERROR
import com.lutty.translate.voiceToText.presentation.DisplayState.SPEAKING
import com.lutty.translate.voiceToText.presentation.DisplayState.WAITING_TO_TALK
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent.Close
import com.lutty.translate.voiceToText.presentation.VoiceToTextState

@Composable
fun VoiceToTextScreen(
  state: VoiceToTextState,
  langCode: String,
  onResult: (String) -> Unit,
  onEvent: (VoiceToTextEvent) -> Unit
) {

  val context = LocalContext.current
  val recordAudioLauncher =
    rememberLauncherForActivityResult(
      contract = RequestPermission(),
      onResult = { isGranted ->
        onEvent(
          VoiceToTextEvent.PermissionResult(
            isGranted = isGranted,
            isPermanentlyDeclined = !isGranted
              && (context as ComponentActivity).shouldShowRequestPermissionRationale(RECORD_AUDIO)
          )
        )
      }
    )

  LaunchedEffect(key1 = recordAudioLauncher) {
    recordAudioLauncher.launch(RECORD_AUDIO)
  }

  Scaffold(
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        FloatingActionButton(
          onClick = {
            if (state.displayState != DISPLAYING_RESULTS) {
              onEvent(VoiceToTextEvent.ToggleRecording(langCode))
            } else onResult(state.spokenText)
          },
          backgroundColor = MaterialTheme.colors.primary,
          contentColor = MaterialTheme.colors.onPrimary,
          modifier = Modifier.size(75.dp)
        ) {
          AnimatedContent(targetState = state.displayState, label = "") { displayState ->
            when (displayState) {
              DISPLAYING_RESULTS -> Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.apply),
                modifier = Modifier.size(50.dp)
              )

              SPEAKING -> Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.stop_recording),
                modifier = Modifier.size(50.dp)
              )

              else -> Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                contentDescription = stringResource(id = R.string.record_audio),
                modifier = Modifier.size(50.dp)
              )
            }
          }

          if (state.displayState == DISPLAYING_RESULTS) {
            IconButton(onClick = { onEvent(VoiceToTextEvent.ToggleRecording(langCode)) }) {
              Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = stringResource(id = R.string.refresh),
                tint = LightBlue
              )
            }
          }
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
          onClick = { onEvent(Close) },
          modifier = Modifier.align(Alignment.CenterStart)
        ) {
          Icon(
            imageVector = Rounded.Close,
            contentDescription = stringResource(id = string.close)
          )
        }

        if (state.displayState == SPEAKING) {
          Text(
            text = stringResource(id = R.string.listening),
            color = LightBlue,
            modifier = Modifier.align(Alignment.Center)
          )
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .padding(bottom = 100.dp)
          .weight(1f)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        AnimatedContent(targetState = state.displayState, label = "") { displayState ->
          when (displayState) {
            WAITING_TO_TALK -> Text(
              text = stringResource(id = R.string.start_talking),
              style = MaterialTheme.typography.h2,
              textAlign = TextAlign.Center
            )

            DISPLAYING_RESULTS -> Text(
              text = state.spokenText,
              style = MaterialTheme.typography.h2,
              textAlign = TextAlign.Center
            )

            SPEAKING -> VoiceRecorderDisplay(
              powerRatios = state.powerRatios,
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
            )

            ERROR -> Text(
              text = state.recordErrorText ?: stringResource(id = R.string.error_unknown),
              style = MaterialTheme.typography.h2,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colors.error
            )

            null -> Unit
          }
        }
      }
    }
  }
}