package com.lutty.translate.translate.data.translate

import com.lutty.NetworkConstants
import com.lutty.translate.core.domain.language.Language
import com.lutty.translate.translate.domain.translate.TranslateError.CLIENT_ERROR
import com.lutty.translate.translate.domain.translate.TranslateError.SERVER_ERROR
import com.lutty.translate.translate.domain.translate.TranslateError.SERVICE_UNAVAILABLE
import com.lutty.translate.translate.domain.translate.TranslateError.UNKNOWN_ERROR
import com.lutty.translate.translate.domain.translate.TranslateException
import com.lutty.translate.translate.domain.translate.TranslationClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class KtorTranslateClient(
  private val httpClient: HttpClient
) : TranslationClient {

  override suspend fun translate(
    fromLanguage: Language,
    toLanguage: Language,
    text: String
  ): String {
    val result = try {
      httpClient.post {
        url(NetworkConstants.BASE_URL + "/translate")
        contentType(Application.Json)
        setBody(
          TranslateDto(
            textToTranslate = text,
            sourceLanguageCode = fromLanguage.langCode,
            targetLanguageCode = toLanguage.langCode
          )
        )
      }
    } catch (e: IOException) {
      throw TranslateException(SERVICE_UNAVAILABLE)
    }

    when (result.status.value) {
      in 200..299 -> Unit
      500 -> throw TranslateException(SERVER_ERROR)
      in 400..499 -> throw TranslateException(CLIENT_ERROR)
      else -> throw TranslateException(UNKNOWN_ERROR)
    }

    return try {
      result.body<TranslatedDto>().translatedText
    } catch (e: Exception) {
      throw TranslateException(SERVER_ERROR)
    }
  }
}