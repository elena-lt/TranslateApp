package com.lutty.translate.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lutty.translate.core.presentation.UiLanguage

@Composable
fun LanguageDropdown(
  selectedLang: UiLanguage,
  isOpen: Boolean,
  onClick: () -> Unit,
  onDismiss: () -> Unit,
  onSelect: (UiLanguage) -> Unit,
  modifier: Modifier = Modifier
) {

  Box(modifier = modifier) {
    DropdownMenu(
      expanded = isOpen,
      onDismissRequest = onDismiss
    ) {
      UiLanguage.langList.forEach { language ->
        LanguageDropdownItem(
          language = language,
          onClick = { onSelect(language) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        model = selectedLang.langIcon,
        contentDescription = selectedLang.language.langName,
        modifier = Modifier.size(30.dp)
      )
      Spacer(modifier = Modifier.width(16.dp))
      Text(text = selectedLang.language.langName)

    }
  }
}