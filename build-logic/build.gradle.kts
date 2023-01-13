plugins {
    `kotlin-dsl`
}

group = "com.rsicarelli.platform"
version = "1.0"

dependencies {
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.compose)
    compileOnly(libs.gradlePlugin.detekt)
    testCompileOnly(kotlin("test"))
}

gradlePlugin {
    with(plugins) {
        register("kpmGradlePlugin") {
            id = "com.rsicarelli.gradle"
            implementationClass = "plugins.KPMGradlePlugin"
        }
    }
}
