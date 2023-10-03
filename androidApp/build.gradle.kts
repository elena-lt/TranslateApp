plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.lutty.translate.android"
  compileSdk = 34
  defaultConfig {
    applicationId = "com.lutty.translate.android"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.2"
  }
  packagingOptions {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  kapt{
    correctErrorTypes = true
  }
}

dependencies {
  implementation(project(":shared"))
  implementation("androidx.compose.ui:ui:1.5.0")
  implementation("androidx.compose.ui:ui-tooling:1.5.0")
  implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
  implementation("androidx.compose.foundation:foundation:1.5.0")
  implementation("androidx.compose.material:material:1.5.0")
  implementation("androidx.activity:activity-compose:1.7.2")

  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  kapt(libs.hilt.compiler)
  kapt(libs.hilt.android.compiler)
}