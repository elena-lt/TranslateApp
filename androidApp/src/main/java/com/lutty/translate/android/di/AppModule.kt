package com.lutty.translate.android.di

import android.content.Context
import com.lutty.translate.database.TranslateDatabase
import com.lutty.translate.translate.data.history.HistoryDataSourceImp
import com.lutty.translate.translate.data.local.DatabaseDriverFactory
import com.lutty.translate.translate.data.remote.HttpClientFactory
import com.lutty.translate.translate.data.translate.KtorTranslateClient
import com.lutty.translate.translate.domain.history.HistoryDataSource
import com.lutty.translate.translate.domain.translate.Translate
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object AppModule {

  @[Provides Singleton]
  fun httpClient(): HttpClient = HttpClientFactory().create()

  @[Provides Singleton]
  fun translateClient(httpClient: HttpClient): KtorTranslateClient =
    KtorTranslateClient(httpClient)

  @[Provides Singleton]
  fun databaseDriver(
    @ApplicationContext context: Context
  ): SqlDriver = DatabaseDriverFactory(context).create()

  @[Provides Singleton]
  fun historyDataSource(driver: SqlDriver): HistoryDataSource =
    HistoryDataSourceImp(TranslateDatabase.invoke(driver))

  @[Provides Singleton]
  fun translateUsecase(client: KtorTranslateClient, dataSource: HistoryDataSource): Translate =
    Translate(
      client = client,
      datasource = dataSource
    )
}