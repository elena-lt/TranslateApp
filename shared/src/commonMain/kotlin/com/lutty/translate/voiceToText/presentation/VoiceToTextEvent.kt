package com.lutty.translate.voiceToText.presentation

sealed class VoiceToTextEvent {
  data object Close : VoiceToTextEvent()
  data class PermissionResult(
    val isGranted: Boolean,
    val isPermanentlyDeclined: Boolean
  ) : VoiceToTextEvent()
  data class ToggleRecording(val langCode: String) : VoiceToTextEvent()
  data object Reset : VoiceToTextEvent()
}