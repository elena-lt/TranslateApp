package com.lutty.translate.voiceToText.presentation

import com.lutty.translate.core.domain.util.toCommonStateFlow
import com.lutty.translate.voiceToText.domain.VoiceToTextParser
import com.lutty.translate.voiceToText.presentation.DisplayState.DISPLAYING_RESULTS
import com.lutty.translate.voiceToText.presentation.DisplayState.ERROR
import com.lutty.translate.voiceToText.presentation.DisplayState.SPEAKING
import com.lutty.translate.voiceToText.presentation.DisplayState.WAITING_TO_TALK
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent.PermissionResult
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent.Reset
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent.ToggleRecording
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
  private val parser: VoiceToTextParser,
  coroutineScope: CoroutineScope? = null
) {

  private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

  private val _state = MutableStateFlow(VoiceToTextState())
  val state = _state
    .combine(parser.state) { state, voiceResult ->
      state.copy(
        spokenText = voiceResult.result,
        recordErrorText = if (state.canRecord) voiceResult.error else "Can't record without permission",
        displayState = when {
          !state.canRecord || voiceResult.error != null -> ERROR
          voiceResult.result.isNotBlank() && !voiceResult.isSpeaking -> DISPLAYING_RESULTS
          voiceResult.isSpeaking -> SPEAKING
          else -> WAITING_TO_TALK
        }
      )
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = VoiceToTextState()
    ).toCommonStateFlow()

  init {
    viewModelScope.launch {
      while (true) {
        if (state.value.displayState == SPEAKING) {
          _state.update { it.copy(powerRatios = it.powerRatios + parser.state.value.powerRatio) }
        }
        delay(50L)
      }
    }
  }

  fun onEvent(event: VoiceToTextEvent) {
    when (event) {
      is PermissionResult -> _state.update { it.copy(canRecord = event.isGranted) }
      Reset -> {
        parser.reset()
        _state.update { VoiceToTextState() }
      }

      is ToggleRecording -> toggleRecording(event.langCode)
      else -> Unit
    }
  }

  private fun toggleRecording(langCode: String) {
    _state.update { it.copy(powerRatios = emptyList()) }
    parser.cancel()
    if (state.value.displayState == SPEAKING) {
      parser.stopListening()
    } else parser.startListening(langCode)
  }
}