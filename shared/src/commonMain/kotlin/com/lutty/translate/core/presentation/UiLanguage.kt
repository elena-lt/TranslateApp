package com.lutty.translate.core.presentation

import com.lutty.translate.core.domain.language.Language

expect class UiLanguage {
  val language: Language

  companion object {
    fun byCode(langCode: String): UiLanguage
    val langList: List<UiLanguage>
  }
}