package com.lutty.translate.core.presentation

import androidx.annotation.DrawableRes
import com.lutty.translate.R
import com.lutty.translate.core.domain.language.Language
import com.lutty.translate.core.domain.language.Language.ARABIC
import com.lutty.translate.core.domain.language.Language.AZERBAIJANI
import com.lutty.translate.core.domain.language.Language.CHINESE
import com.lutty.translate.core.domain.language.Language.CZECH
import com.lutty.translate.core.domain.language.Language.DANISH
import com.lutty.translate.core.domain.language.Language.DUTCH
import com.lutty.translate.core.domain.language.Language.ENGLISH
import com.lutty.translate.core.domain.language.Language.FINNISH
import com.lutty.translate.core.domain.language.Language.FRENCH
import com.lutty.translate.core.domain.language.Language.GERMAN
import com.lutty.translate.core.domain.language.Language.GREEK
import com.lutty.translate.core.domain.language.Language.HEBREW
import com.lutty.translate.core.domain.language.Language.HINDI
import com.lutty.translate.core.domain.language.Language.HUNGARIAN
import com.lutty.translate.core.domain.language.Language.INDONESIAN
import com.lutty.translate.core.domain.language.Language.IRISH
import com.lutty.translate.core.domain.language.Language.ITALIAN
import com.lutty.translate.core.domain.language.Language.JAPANESE
import com.lutty.translate.core.domain.language.Language.KOREAN
import com.lutty.translate.core.domain.language.Language.PERSIAN
import com.lutty.translate.core.domain.language.Language.POLISH
import com.lutty.translate.core.domain.language.Language.PORTUGUESE
import com.lutty.translate.core.domain.language.Language.SLOVAK
import com.lutty.translate.core.domain.language.Language.SPANISH
import com.lutty.translate.core.domain.language.Language.SWEDISH
import com.lutty.translate.core.domain.language.Language.TURKISH
import com.lutty.translate.core.domain.language.Language.UKRAINIAN
import java.util.Locale

actual class UiLanguage(
  @DrawableRes val langIcon: Int,
  actual val language: Language
) {

  fun toLocale(): Locale? =
    when (language) {
      ENGLISH -> Locale.ENGLISH
      GERMAN -> Locale.GERMAN
      KOREAN -> Locale.KOREAN
      JAPANESE -> Locale.JAPANESE
      FRENCH -> Locale.FRENCH
      CHINESE -> Locale.CHINESE
      ITALIAN -> Locale.ITALIAN
      else -> null
    }

  actual companion object {
    actual fun byCode(langCode: String): UiLanguage =
      langList.find { lang -> lang.language.langCode == langCode }
        ?: throw IllegalStateException("Language is not supported")

    actual val langList: List<UiLanguage>
      get() = Language.values().map { language ->
        UiLanguage(
          language = language,
          langIcon = when (language) {
            ENGLISH -> R.drawable.english
            ARABIC -> R.drawable.arabic
            AZERBAIJANI -> R.drawable.azerbaijani
            CHINESE -> R.drawable.chinese
            CZECH -> R.drawable.czech
            DANISH -> R.drawable.danish
            DUTCH -> R.drawable.dutch
            FINNISH -> R.drawable.finnish
            FRENCH -> R.drawable.french
            GERMAN -> R.drawable.german
            GREEK -> R.drawable.greek
            HEBREW -> R.drawable.hebrew
            HINDI -> R.drawable.hindi
            HUNGARIAN -> R.drawable.hungarian
            INDONESIAN -> R.drawable.indonesian
            IRISH -> R.drawable.irish
            ITALIAN -> R.drawable.italian
            JAPANESE -> R.drawable.japanese
            KOREAN -> R.drawable.korean
            PERSIAN -> R.drawable.persian
            POLISH -> R.drawable.polish
            PORTUGUESE -> R.drawable.portuguese
            SLOVAK -> R.drawable.slovak
            SPANISH -> R.drawable.spanish
            SWEDISH -> R.drawable.swedish
            TURKISH -> R.drawable.turkish
            UKRAINIAN -> R.drawable.ukrainian
          }
        )
      }.sortedBy { it.language.langName }
  }
}