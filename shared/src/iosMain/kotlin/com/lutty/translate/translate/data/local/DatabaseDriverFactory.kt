package com.lutty.translate.translate.data.local

import com.lutty.translate.database.TranslateDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
  actual fun create(): SqlDriver {
    return NativeSqliteDriver(
      TranslateDatabase.Schema,
      "Translate.db"
    )
  }
}