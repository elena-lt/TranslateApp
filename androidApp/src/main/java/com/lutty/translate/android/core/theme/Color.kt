package com.lutty.translate.android.core.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import com.lutty.translate.core.presentation.Colors

val AccentViolet = Color(Colors.AccentViolet)
val TextBlack = Color(Colors.TextBlack)
val DarkGray = Color(Colors.DarkGrey)
val LightBlueGray = Color(Colors.LightBlueGrey)
val LightBlur = Color(Colors.LightBlue)

val lightColors = lightColors(
  primary = AccentViolet,
  onPrimary = White,
  background = LightBlueGray,
  onBackground = TextBlack,
  surface = White,
  onSurface = TextBlack
)

val darkColors = darkColors(
  primary = AccentViolet,
  background = DarkGray,
  onPrimary = White,
  onBackground = White,
  surface = DarkGray,
  onSurface = White
)
