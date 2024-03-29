package org.gradle.api

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
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

internal inline fun <reified T : Any> Project.withExtension(
    block: T.() -> Unit,
): T? = extensions.findByType<T>()?.apply(block)

internal fun Project.androidCommonExtension(
    block: CommonExtension<*, *, *, *>.() -> Unit,
) {
    (extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("There is no android plugin configured for the module"))
        .apply(block)
}

internal val Project.projectNamespace: String
    get() {
        val modulePath = path.split(":").joinToString(".") { it }
        return "${rootProject.group}$modulePath"
    }
