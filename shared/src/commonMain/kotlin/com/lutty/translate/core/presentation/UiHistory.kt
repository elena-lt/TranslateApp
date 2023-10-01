package com.lutty.translate.core.presentation

import com.lutty.translate.translate.domain.history.History

data class UiHistory(
  val id: Long,
  val fromLanguage: UiLanguage,
  val fromText: String,
  val toLangCode: UiLanguage,
  val toText: String
){

  companion object {
    fun toUiHistory(history: History): UiHistory =
      UiHistory(
        id = history.id ?: 0,
        fromText = history.fromText,
        toText = history.toText,
        fromLanguage = UiLanguage.byCode(history.fromLangCode),
        toLangCode = UiLanguage.byCode(history.toLangCode)
      )
  }
}