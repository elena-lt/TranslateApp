package com.lutty.translate.translate.data.history

import com.lutty.translate.core.domain.util.CommonFlow
import com.lutty.translate.core.domain.util.toCommonFlow
import com.lutty.translate.database.TranslateDatabase
import com.lutty.translate.translate.domain.history.History
import com.lutty.translate.translate.domain.history.HistoryDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class HistoryDataSourceImp(
  private val database: TranslateDatabase
) : HistoryDataSource {

  override fun getHistory(): CommonFlow<List<History>> {
    return database.translateQueries.getHistory()
      .asFlow()
      .mapToList()
      .map { history ->
        history.map { item -> item.toHistory()  }
      }
      .toCommonFlow()
  }

  override suspend fun insertHistoryItem(history: History) {
    database.translateQueries.insertHistoryEntity(
      id = history.id,
      fromLangCode = history.fromLangCode,
      fromText = history.fromText,
      toLangCode = history.toLangCode,
      toText = history.toText,
      timestamp = Clock.System.now().toEpochMilliseconds()
    )
  }
}