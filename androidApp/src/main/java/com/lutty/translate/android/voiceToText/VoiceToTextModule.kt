package com.lutty.translate.android.voiceToText

import android.app.Application
import com.lutty.translate.android.voiceToText.data.AndroidVoiceToTextParser
import com.lutty.translate.voiceToText.domain.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object VoiceToTextModule {

  @[Provides ViewModelScoped]
  fun provideVoiceToTextParser(app: Application): VoiceToTextParser =
    AndroidVoiceToTextParser(app)
}