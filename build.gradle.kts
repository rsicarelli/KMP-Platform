@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.android)
        classpath(libs.gradlePlugin.detekt)
    }
}


plugins {
    alias(libs.plugins.rsicarelli.kmpgradleplatform)
}

installDefaults()
installDetekt()
