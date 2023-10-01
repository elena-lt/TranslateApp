package com.lutty.translate.translate.domain.translate

import com.lutty.translate.core.domain.language.Language
import com.lutty.translate.core.domain.util.Resource
import com.lutty.translate.translate.data.translate.KtorTranslateClient
import com.lutty.translate.translate.domain.history.History
import com.lutty.translate.translate.domain.history.HistoryDataSource

class Translate(
  private val client: KtorTranslateClient,
  private val datasource: HistoryDataSource
) {

  suspend operator fun invoke(
    fromLang: Language,
    fromText: String,
    toLang: Language
  ): Resource<String> {
    return try {
      val translatedText =
        client.translate(
          fromLanguage = fromLang,
          text = fromText,
          toLanguage = toLang
        )

      datasource.insertHistoryItem(
        History(
          id = null,
          fromLangCode = fromLang.langCode,
          fromText = fromText,
          toLangCode = toLang.langCode,
          toText = translatedText
        )
      )

      return Resource.Success(translatedText)
    } catch (e: TranslateException) {
      e.printStackTrace()
      Resource.Error(e)
    }
  }
}