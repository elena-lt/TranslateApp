package com.lutty.translate.android.voiceToText.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutty.translate.voiceToText.domain.VoiceToTextParser
import com.lutty.translate.voiceToText.presentation.VoiceToTextEvent
import com.lutty.translate.voiceToText.presentation.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
  private val parser: VoiceToTextParser
) : ViewModel() {

  private val viewModel by lazy {
    VoiceToTextViewModel(
      parser = parser,
      coroutineScope = viewModelScope
    )
  }

  val state = viewModel.state

  fun onEvent(event: VoiceToTextEvent) =
    viewModel.onEvent(event)
}