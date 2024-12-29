// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

object Versions {
    const val pedroModuleVersion = "0.5.0-beta2"
    const val ftcModuleVersion = "0.5.0-beta2"
    const val coreModuleVersion = "0.5.0-beta2"
}