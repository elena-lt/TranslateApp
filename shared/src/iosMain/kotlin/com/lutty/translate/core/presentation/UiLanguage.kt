package com.lutty.translate.core.presentation

import com.lutty.translate.core.domain.language.Language

actual class UiLanguage(
  val imageName: String,
  actual val language: Language
) {

  actual companion object {
    actual fun byCode(langCode: String): UiLanguage =
      langList.find { lang -> lang.language.langCode == langCode }
        ?: throw IllegalStateException("Language is not supported")

    actual val langList: List<UiLanguage>
      get() = Language.values().map { lang ->
        UiLanguage(
          imageName = lang.langName.lowercase(),
          language = lang
        )
      }
  }
}