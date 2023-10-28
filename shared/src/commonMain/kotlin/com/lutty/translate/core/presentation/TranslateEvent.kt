package com.lutty.translate.core.presentation

sealed class TranslateEvent {
  data class ChooseFromLanguage(val lang: UiLanguage) : TranslateEvent()
  data class ChooseToLanguage(val lang: UiLanguage) : TranslateEvent()
  object StopChoosingLanguage : TranslateEvent()
  object SwapLanguages : TranslateEvent()
  data class ChangeTranslationText(val text: String) : TranslateEvent()
  object Translate : TranslateEvent()
  object OpenFromLangDropDown : TranslateEvent()
  object OpenToLangDropDown : TranslateEvent()
  object CloseTranslation : TranslateEvent()
  data class SelectHistoryItem(val item: UiHistory) : TranslateEvent()
  object EditTranslation : TranslateEvent()
  object RecordAudio : TranslateEvent()
  data class SubmitVoiceResult(val result: String?) : TranslateEvent()
  object OnErrorSeen : TranslateEvent()
}