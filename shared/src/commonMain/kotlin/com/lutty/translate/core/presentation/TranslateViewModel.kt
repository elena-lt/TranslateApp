package com.lutty.translate.core.presentation

import com.lutty.translate.core.domain.util.Resource
import com.lutty.translate.core.domain.util.toCommonStateFlow
import com.lutty.translate.core.presentation.TranslateEvent.ChangeTranslationText
import com.lutty.translate.core.presentation.TranslateEvent.ChooseFromLanguage
import com.lutty.translate.core.presentation.TranslateEvent.ChooseToLanguage
import com.lutty.translate.core.presentation.TranslateEvent.CloseTranslation
import com.lutty.translate.core.presentation.TranslateEvent.EditTranslation
import com.lutty.translate.core.presentation.TranslateEvent.OnErrorSeen
import com.lutty.translate.core.presentation.TranslateEvent.OpenFromLangDropDown
import com.lutty.translate.core.presentation.TranslateEvent.OpenToLangDropDown
import com.lutty.translate.core.presentation.TranslateEvent.SelectHistoryItem
import com.lutty.translate.core.presentation.TranslateEvent.StopChoosingLanguage
import com.lutty.translate.core.presentation.TranslateEvent.SubmitVoiceResult
import com.lutty.translate.core.presentation.TranslateEvent.SwapLanguages
import com.lutty.translate.core.presentation.UiHistory.Companion.toUiHistory
import com.lutty.translate.translate.domain.history.HistoryDataSource
import com.lutty.translate.translate.domain.translate.Translate
import com.lutty.translate.translate.domain.translate.TranslateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class TranslateViewModel(
  private val translate: Translate,
  private val historyDataSource: HistoryDataSource,
  private val coroutineScope: CoroutineScope? = null
) {

  private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

  private val _state = MutableStateFlow(TranslateState())
  val state = combine(_state, historyDataSource.getHistory()) { state, history ->
    if (state.history != history) {
      state.copy(
        history = history.map(::toUiHistory)
      )
    } else state
  }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = TranslateState()
    ).toCommonStateFlow()


  private var translateJob: Job? = null

  private fun translateText(state: TranslateState) {
    if (state.isTranslating || state.fromText.isBlank()) return

    translateJob = viewModelScope.launch {
      _state.update { state.copy(isTranslating = true) }

      val result = translate(
        fromText = state.fromText,
        fromLang = state.fromLanguage.language,
        toLang = state.toLanguage.language
      )

      when (result) {
        is Resource.Success -> _state.update {
          state.copy(
            isTranslating = false,
            toText = result.data
          )
        }
        is Resource.Error -> _state.update {
          it.copy(
            isTranslating = false,
            error = (result.throwable as? TranslateException)?.error
          )
        }
      }
    }
  }

  fun onEvent(event: TranslateEvent) {
    when (event) {
      is ChangeTranslationText -> _state.update { it.copy(fromText = event.text) }
      is ChooseFromLanguage -> _state.update {
        it.copy(
          isChoosingFromLanguage = false,
          fromLanguage = event.lang
        )
      }
      is ChooseToLanguage -> {
        _state.updateAndGet { it.copy(isChoosingToLanguage = false, toLanguage = event.lang) }
          .let(::translateText)
      }
      CloseTranslation -> _state.update {
        it.copy(
          isTranslating = false,
          fromText = "",
          toText = ""
        )
      }
      EditTranslation -> {
        if (state.value.toText != null) {
          _state.update { it.copy(toText = null, isTranslating = false) }
        }
      }
      OnErrorSeen -> _state.update { it.copy(error = null) }
      OpenFromLangDropDown -> _state.update { it.copy(isChoosingFromLanguage = true) }
      OpenToLangDropDown -> _state.update { it.copy(isChoosingToLanguage = true) }
      is SelectHistoryItem -> _state.update {
        translateJob?.cancel()
        it.copy(
          fromText = event.item.fromText,
          toText = event.item.toText,
          fromLanguage = UiLanguage.byCode(event.item.fromLanguage.language.langCode),
          toLanguage = UiLanguage.byCode(event.item.toLangCode.language.langCode),
          isTranslating = false
        )
      }
      StopChoosingLanguage -> _state.update {
        it.copy(
          isChoosingToLanguage = false,
          isChoosingFromLanguage = false
        )
      }
      is SubmitVoiceResult -> _state.update {
        it.copy(
          fromText = event.result ?: it.fromText,
          isTranslating = if (event.result != null) false else it.isTranslating,
          toText = if (event.result != null) null else it.toText
        )
      }
      SwapLanguages -> _state.update {
        it.copy(
          fromLanguage = it.toLanguage, toLanguage = it.fromLanguage,
          fromText = it.toText ?: "",
          toText = if (it.toText != null) it.fromText else null
        )
      }
      TranslateEvent.Translate -> translateText(state.value)
      else -> Unit
    }
  }
}