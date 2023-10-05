package com.lutty.translate.translate.data.history

import com.lutty.translate.translate.domain.history.History
import database.HistoryEntity

fun HistoryEntity.toHistory(): History =
  History(
    id = id,
    fromText = fromText,
    fromLangCode = fromLangCode,
    toLangCode = toLangCode,
    toText = toText
  )