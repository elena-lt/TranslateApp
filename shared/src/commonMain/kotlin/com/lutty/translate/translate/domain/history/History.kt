package com.lutty.translate.translate.domain.history

data class History(
  val id: Long?,
  val fromLangCode: String,
  val fromText: String,
  val toLangCode: String,
  val toText: String
)