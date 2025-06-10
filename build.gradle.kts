// Project-level build.gradle (build.gradle.kts)
plugins {
    // Плагины для Android и Kotlin
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false


    // Плагин для Google Services (Firebase)
    id("com.google.gms.google-services") version "4.3.15" apply true
}
