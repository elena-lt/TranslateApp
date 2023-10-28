package com.lutty.translate.voiceToText.domain

import com.lutty.translate.core.domain.util.CommonStateFlow

interface VoiceToTextParser {

  val state: CommonStateFlow<VoiceToTextParserState>

  fun startListening(langCode: String)
  fun stopListening()
  fun cancel()
  fun reset()
}