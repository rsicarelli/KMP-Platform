buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.android)
        classpath(libs.gradlePlugin.compose)
        classpath(libs.gradlePlugin.detekt)
    }
}

plugins {
    alias(libs.plugins.rsicarelli.kmplatform)
}

group = "com.rsicarelli.kmplatform"
version = libs.versions.kmplatform.get()

installDefaults()
installDetekt()
