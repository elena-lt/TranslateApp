package com.lutty.translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TranslateApp() {

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Welcome to translate app")
  }
}