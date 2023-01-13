plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.compose)
    testCompileOnly(kotlin("test"))
}

gradlePlugin {
    isAutomatedPublishing = false

    with(plugins) {
        register("kpmGradlePlugin") {
            id = "com.rsicarelli.gradle"
            implementationClass = "plugins.KPMGradlePlugin"
        }
    }
}
