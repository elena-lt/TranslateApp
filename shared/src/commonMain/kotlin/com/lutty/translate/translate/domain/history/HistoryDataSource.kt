package com.lutty.translate.translate.domain.history

import com.lutty.translate.core.domain.util.CommonFlow

interface HistoryDataSource {
  fun getHistory(): CommonFlow<List<History>>
  suspend fun insertHistoryItem(history: History)
}