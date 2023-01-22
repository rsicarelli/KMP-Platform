package decorators

import config.AndroidConfig
import config.PlatformPublicationTarget
import config.PlatformPublicationTarget.Android
import config.PlatformPublicationTarget.Jvm
import config.PlatformPublicationTarget.Multiplatform
import config.PublicationConfig
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.withExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension

private const val JAVADOC_JAR_TASK_NAME = "javadocJar"

private fun Project.ensureJavadocJarTask(): Task =
    tasks.findByName(JAVADOC_JAR_TASK_NAME) ?: createJavadocJarTask()

private fun Project.createJavadocJarTask(): Task =
    tasks.create<Jar>(JAVADOC_JAR_TASK_NAME).apply {
        archiveClassifier.set("javadoc")
    }

internal fun Project.setComponentPublication(
    publicationTarget: PlatformPublicationTarget,
    artefactId: String,
    publicationConfig: PublicationConfig = requireDefaults(),
) {
    plugins.apply("maven-publish")
    group = publicationConfig.group

    when (publicationTarget) {
        Multiplatform -> setMultiplatformLibraryPublication(artefactId)
        Android -> setAndroidLibraryPublication(artefactId)
        Jvm -> setJvmLibraryPublication(artefactId)
    }

    withExtension<PublishingExtension> {
        publications.withType<MavenPublication>().configureEach {
            defaultMavenPublication(project, publicationConfig, artefactId)
        }
    }

    setupPublicationRepository(publicationConfig)
}

private fun Project.setMultiplatformLibraryPublication(artefactId: String) =
    withExtension<AndroidLibraryExtension> {
        publishing {
            multipleVariants(artefactId) {
                with(requireDefaults<AndroidConfig>()) {
                    variants.forEach {
                        includeBuildTypeValues(it)
                    }
                }
            }
        }
    }

internal fun Project.setJvmLibraryPublication(artefactId: String) {
    val sourceJar by tasks.registering(Jar::class) {
        from(the<JavaPluginExtension>().sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }

    withExtension<PublishingExtension> {
        publications {
            create<MavenPublication>(artefactId) {
                from(components["java"])
                artifact(sourceJar.get())
            }
        }
    }
}

internal fun Project.setAndroidLibraryPublication(artefactId: String) {
    val sourceJarTask by tasks.creating(Jar::class) {
        from(the<AndroidLibraryExtension>().sourceSets.getByName("main").java.srcDirs)
        archiveClassifier.set("source")
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>(artefactId) {
                from(components["release"])
                artifact(sourceJarTask)
            }
        }
    }
}

private fun MavenPublication.defaultMavenPublication(
    project: Project,
    config: PublicationConfig,
    artefactId: String,
) = pom {
    groupId = config.group
    artifactId = artefactId

    name.set(project.name)
    description.set(project.description)
    url.set(config.projectUrl)

    licenses {
        config.license.forEach {
            license {
                name.set(it.licenseName)
                url.set(it.licenseUrl)
            }
        }
    }

    scm {
        url.set(config.projectUrl)
        connection.set(config.scmUrl)
        developerConnection.set(config.scmUrl)
    }
}.also {
    artifact(project.ensureJavadocJarTask())
}

internal fun Project.setupPublicationRepository(config: PublicationConfig) {
    extensions.configure<PublishingExtension> {
        if (config.signing != null) {
            plugins.apply("signing")

            extensions.configure<SigningExtension> {
                useInMemoryPgpKeys(config.signing.key, config.signing.password)
                sign(publications)
            }
        }

        repositories {
            maven(config.mavenUrl) {
                credentials {
                    username = config.credentials.username
                    password = config.credentials.password
                }
            }
        }
    }
}
