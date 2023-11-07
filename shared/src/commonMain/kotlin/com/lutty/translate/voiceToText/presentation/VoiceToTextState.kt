package com.lutty.translate.voiceToText.presentation

data class VoiceToTextState(
  val powerRatios: List<Float> = emptyList(),
  val spokenText: String = "",
  val canRecord: Boolean = false,
  val recordErrorText: String? = null,
  val displayState: DisplayState? = null
)