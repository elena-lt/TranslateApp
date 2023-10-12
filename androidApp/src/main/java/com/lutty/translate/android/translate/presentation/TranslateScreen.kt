package com.lutty.translate.android.translate.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.lutty.translate.android.R
import com.lutty.translate.android.translate.presentation.components.LanguageDropdown
import com.lutty.translate.android.translate.presentation.components.SwapLanguagesButton
import com.lutty.translate.android.translate.presentation.components.TranslateTextField
import com.lutty.translate.core.presentation.TranslateEvent
import com.lutty.translate.core.presentation.TranslateEvent.ChangeTranslationText
import com.lutty.translate.core.presentation.TranslateEvent.CloseTranslation
import com.lutty.translate.core.presentation.TranslateEvent.EditTranslation
import com.lutty.translate.core.presentation.TranslateEvent.OpenToLangDropDown
import com.lutty.translate.core.presentation.TranslateEvent.SwapLanguages
import com.lutty.translate.core.presentation.TranslateState

@Composable
fun TranslateScreen(
  state: TranslateState,
  onEvent: (TranslateEvent) -> Unit
) {
  val context = LocalContext.current

  Scaffold(floatingActionButton = {}) { paddingValues ->
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
        val kearboardController = LocalSoftwareKeyboardController.current

        TranslateTextField(
          fromText = state.fromText,
          toText = state.toText,
          isTranslating = state.isTranslating,
          fromLanguage = state.fromLanguage,
          toLanguage = state.toLanguage,
          onTranslateClick = {
            kearboardController?.hide()
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
          onSpeakerClick = { /*TODO*/ },
          onTextFieldClick = { onEvent(EditTranslation) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}