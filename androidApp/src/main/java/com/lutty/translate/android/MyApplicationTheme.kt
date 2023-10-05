package com.lutty.translate.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lutty.translate.android.core.theme.darkColors
import com.lutty.translate.android.core.theme.lightColors

@Composable
fun TranslateTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (darkTheme) darkColors else lightColors
  val font = FontFamily(
    Font(
      resId = R.font.sf_pro_text_medium,
      weight = FontWeight.Normal
    ),
    Font(
      resId = R.font.sf_pro_text_medium,
      weight = FontWeight.Medium
    ),
    Font(
      resId = R.font.sf_pro_text_bold,
      weight = FontWeight.Bold
    )
  )
  val typography = Typography(
    h1 = TextStyle(
      fontFamily = font,
      fontWeight = FontWeight.Bold,
      fontSize = 30.sp
    ),
    h2 = TextStyle(
      fontFamily = font,
      fontWeight = FontWeight.Bold,
      fontSize = 24.sp
    ),
    h3 = TextStyle(
      fontFamily = font,
      fontWeight = FontWeight.Medium,
      fontSize = 18.sp
    ),
    body1 = TextStyle(
      fontFamily = font,
      fontWeight = FontWeight.Normal,
      fontSize = 14.sp
    ),
    body2 = TextStyle(
      fontFamily = font,
      fontWeight = FontWeight.Normal,
      fontSize = 12.sp
    )
  )
  val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
  )

  MaterialTheme(
    colors = colors,
    typography = typography,
    shapes = shapes,
    content = content
  )
}
