package com.lutty.translate.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.lutty.translate.android.R
import com.lutty.translate.android.translate.presentation.components.LanguageDropdown
import com.lutty.translate.android.translate.presentation.components.SwapLanguagesButton
import com.lutty.translate.android.translate.presentation.components.TranslateHistoryItem
import com.lutty.translate.android.translate.presentation.components.TranslateTextField
import com.lutty.translate.android.translate.presentation.components.rememberTextToSpeech
import com.lutty.translate.core.presentation.TranslateEvent
import com.lutty.translate.core.presentation.TranslateEvent.ChangeTranslationText
import com.lutty.translate.core.presentation.TranslateEvent.CloseTranslation
import com.lutty.translate.core.presentation.TranslateEvent.EditTranslation
import com.lutty.translate.core.presentation.TranslateEvent.OnErrorSeen
import com.lutty.translate.core.presentation.TranslateEvent.OpenToLangDropDown
import com.lutty.translate.core.presentation.TranslateEvent.RecordAudio
import com.lutty.translate.core.presentation.TranslateEvent.SelectHistoryItem
import com.lutty.translate.core.presentation.TranslateEvent.SwapLanguages
import com.lutty.translate.core.presentation.TranslateState
import com.lutty.translate.translate.domain.translate.TranslateError.CLIENT_ERROR
import com.lutty.translate.translate.domain.translate.TranslateError.SERVER_ERROR
import com.lutty.translate.translate.domain.translate.TranslateError.SERVICE_UNAVAILABLE
import com.lutty.translate.translate.domain.translate.TranslateError.UNKNOWN_ERROR
import java.util.Locale

@Composable
fun TranslateScreen(
  state: TranslateState,
  onEvent: (TranslateEvent) -> Unit
) {
  val context = LocalContext.current

  LaunchedEffect(key1 = state.error) {
    val message = when (state.error) {
      SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
      CLIENT_ERROR -> context.getString(R.string.client_error)
      SERVER_ERROR -> context.getString(R.string.server_error)
      UNKNOWN_ERROR -> context.getString(R.string.error_unknown)
      else -> null
    }

    message?.let {
      Toast.makeText(context, it, Toast.LENGTH_LONG).show()
      onEvent(OnErrorSeen)
    }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onEvent(RecordAudio) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = Modifier.size(75.dp)
      ) {
        Icon(
          imageVector = ImageVector.vectorResource(id = R.drawable.mic),
          contentDescription = stringResource(id = R.string.record_audio)
        )
      }
    },
    floatingActionButtonPosition = FabPosition.Center
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          LanguageDropdown(
            selectedLang = state.fromLanguage,
            isOpen = state.isChoosingFromLanguage,
            onClick = { onEvent(TranslateEvent.OpenFromLangDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelect = { onEvent(TranslateEvent.ChooseFromLanguage(it)) }
          )
          Spacer(modifier = Modifier.weight(1f))
          SwapLanguagesButton(onClick = { onEvent(SwapLanguages) })
          Spacer(modifier = Modifier.weight(1f))
          LanguageDropdown(
            selectedLang = state.toLanguage,
            isOpen = state.isChoosingToLanguage,
            onClick = { onEvent(OpenToLangDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelect = { onEvent(TranslateEvent.ChooseToLanguage(it)) }
          )
        }
      }

      item {
        val clipboardmanager = LocalClipboardManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val tts = rememberTextToSpeech()

        TranslateTextField(
          fromText = state.fromText,
          toText = state.toText,
          isTranslating = state.isTranslating,
          fromLanguage = state.fromLanguage,
          toLanguage = state.toLanguage,
          onTranslateClick = {
            keyboardController?.hide()
            onEvent(TranslateEvent.Translate)
          },
          onTextChange = {
            onEvent(ChangeTranslationText(it))
          },
          onCopyClick = {
            clipboardmanager.setText(buildAnnotatedString { append(it) })
            Toast.makeText(
              context,
              context.getString(R.string.copied_to_clipboard),
              Toast.LENGTH_SHORT
            ).show()
            onEvent(ChangeTranslationText(it))
          },
          onCloseClick = { onEvent(CloseTranslation) },
          onSpeakerClick = {
            tts.language = state.toLanguage.toLocale() ?: Locale.ENGLISH
            tts.speak(state.toText, TextToSpeech.QUEUE_FLUSH, null, null)
          },
          onTextFieldClick = { onEvent(EditTranslation) },
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        if (state.history.isNotEmpty()) {
          Text(
            text = stringResource(id = R.string.history),
            style = MaterialTheme.typography.h2
          )
        }
      }

      items(state.history) { historyItem ->
        TranslateHistoryItem(
          item = historyItem,
          onClick = { onEvent(SelectHistoryItem(historyItem)) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}