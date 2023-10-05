package com.lutty.translate.translate.domain.translate

import com.lutty.translate.core.domain.language.Language

interface TranslationClient {

  /**
   * Processes given [text] provided in language [fromLanguage] and translates it to
   * [toLanguage].
   */
  suspend fun translate(
    fromLanguage: Language,
    toLanguage: Language,
    text: String
  ): String


}