package decorators

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

internal const val PROJECT_DEFAULTS_KEY = "com.rsicarelli.platform.gradle.plugins"

/**
 * Retrieve a reified object from Project's extra.
 *
 * If null/not present, an exception will be thrown.
 *
 * @param key: default is [PROJECT_DEFAULTS_KEY]
 * */
internal inline fun <reified T : Any> Project.requireDefaults(key: String = PROJECT_DEFAULTS_KEY): T =
    requireNotNull(getDefaults(key)) { "Defaults not found for type ${T::class}" }

private inline fun <reified T : Any> Project.getDefaults(key: String): T? =
    getDefaults(key) { it as? T }

private fun <T : Any> Project.getDefaults(key: String, mapper: (Any) -> T?): T? =
    getDefaultsList(key)?.asSequence()?.mapNotNull(mapper)?.firstOrNull()
        ?: parent?.getDefaults(key, mapper)

@Suppress("UNCHECKED_CAST")
private fun Project.getDefaultsList(key: String): MutableList<Any>? =
    extra.takeIf { it.has(key) }?.get(key) as ArrayList<Any>?
