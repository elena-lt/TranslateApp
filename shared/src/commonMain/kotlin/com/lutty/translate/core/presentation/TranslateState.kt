package com.lutty.translate.core.presentation

import com.lutty.translate.translate.domain.translate.TranslateError

data class TranslateState(
  val fromText: String = "",
  val toText: String? = null,
  val isTranslating: Boolean = false,
  val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
  val toLanguage: UiLanguage = UiLanguage.byCode("en"),
  val isChoosingFromLanguage: Boolean = false,
  val isChoosingToLanguage: Boolean = false,
  val error: TranslateError? = null,
  val history: List<UiHistory> = emptyList()
)