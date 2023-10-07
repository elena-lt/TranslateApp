package com.lutty.translate.android.translate.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.lutty.translate.core.presentation.UiLanguage

@Composable
fun LanguageDropdownItem(
  language: UiLanguage,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  DropdownMenuItem(
    onClick = onClick,
    modifier = modifier
  ) {
    Image(
      painter = painterResource(
        id = language.langIcon
      ),
      contentDescription = language.language.langName,
      modifier = Modifier.size(40.dp)
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(text = language.language.langName.lowercase().capitalize(Locale.current))
  }
}