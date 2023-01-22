package org.gradle.api

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

internal val Project.catalogs
    get() = extensions.getByType<VersionCatalogsExtension>()

internal val Project.libs: VersionCatalog get() = catalogs.named("libs")

internal fun VersionCatalog.version(reference: String): String? = findVersion(reference)
    .orElse(null)
    ?.toString()

internal val Project.compose
    get() = extensions.getByType<ComposeExtension>()

internal fun Project.checkIsRootProject() {
    check(rootProject == this) { "Must be called on a root project" }
}

inline fun <reified T : Any> Project.withExtension(
    crossinline block: T.() -> Unit,
) {
    extensions.findByType<T>()?.let { it.block() }
}

internal val Project.projectNamespace: String
    get() {
        val parentName = parent?.parent?.name?.lowercase()

        return "com.rsicarelli.$parentName.$name"
    }
