import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

group = "com.rsicarelli.kmplatform"
version = libs.versions.kmplatform.get()

dependencies {
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.compose)
    compileOnly(libs.gradlePlugin.detekt)
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

gradlePlugin {
    with(plugins) {
        register("KMPlatform") {
            id = "com.rsicarelli.kmplatform"
            version = libs.versions.kmplatform.get()
            implementationClass = "KMPlatformPlugin"
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("versionCatalog") {
            artifactId = "versioncatalog"
            version = libs.versions.kmplatform.get()
            from(components["versionCatalog"])
        }
    }
}

//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//        allWarningsAsErrors = false
//        freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
//        jvmTarget = "11"
//    }
//}
//
//extensions.configure<JavaPluginExtension> {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}
