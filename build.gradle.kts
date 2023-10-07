plugins {
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.android.application).apply(false)
  alias(libs.plugins.android.compose).apply(false)
  alias(libs.plugins.sql.delight).apply(false)
  alias(libs.plugins.hilt).apply(false)
  alias(libs.plugins.android.multiplatform).apply(false)
  alias(libs.plugins.cocoapods).apply(false)
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
