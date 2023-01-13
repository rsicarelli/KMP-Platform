package org.gradle.api

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

internal val Project.catalogs
    get() = extensions.getByType<VersionCatalogsExtension>()

internal val Project.libs: VersionCatalog get() = catalogs.named("libs")

internal val Project.compose
    get() = extensions.getByType<ComposeExtension>()
