package com.lutty.translate.android.translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutty.translate.core.presentation.TranslateEvent
import com.lutty.translate.core.presentation.TranslateViewModel
import com.lutty.translate.translate.domain.history.HistoryDataSource
import com.lutty.translate.translate.domain.translate.Translate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
  private val translate: Translate,
  private val historyDataSource: HistoryDataSource
) : ViewModel() {

  private val viewModel by lazy {
    TranslateViewModel(
      translate = translate,
      historyDataSource = historyDataSource,
      coroutineScope = viewModelScope
    )
  }

  val state = viewModel.state

  fun onEvent(event: TranslateEvent) {
    viewModel.onEvent(event)
  }
}