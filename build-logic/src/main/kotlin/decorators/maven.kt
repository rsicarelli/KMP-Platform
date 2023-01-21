package decorators

import com.android.build.gradle.LibraryExtension
import config.PublicationConfig
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.the
import org.gradle.plugins.signing.SigningExtension

internal fun Project.configureJvmLibraryPublication(
    config: PublicationConfig = requireDefaults(),
    version: String,
    artefactId: String,
) {
    applyMavenPublish()

    val sourceJar by tasks.registering(Jar::class) {
        from(project.the<JavaPluginExtension>().sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>(artefactId) {
                from(components["java"])
                artifact(sourceJar.get())

                group = config.group
                this.version = version
                this.artifactId = artefactId

                setupPublicationPom(project, config)
            }
        }
    }

    setupPublicationRepository(config)
}

internal fun Project.setAndroidLibraryPublication(
    config: PublicationConfig = requireDefaults(),
    version: String,
    artefactId: String,
) {
    applyMavenPublish()

    val androidExtension = extensions.getByType<LibraryExtension>()

    val sourceJarTask by tasks.creating(Jar::class) {
        from(androidExtension.sourceSets.getByName("main").java.srcDirs)
        archiveClassifier.set("source")
    }

    afterEvaluate {
        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>(artefactId) {
                    from(components["release"])
                    artifact(sourceJarTask)

                    groupId = config.group
                    this.version = version
                    artifactId = artefactId

                    setupPublicationPom(project, config)
                }
            }
        }
    }

    setupPublicationRepository(config)
}

internal fun MavenPublication.setupPublicationPom(
    project: Project,
    config: PublicationConfig,
) {
    artifact(project.ensureJavadocJarTask())

    pom {
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
    }
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

private const val JAVADOC_JAR_TASK_NAME = "javadocJar"

private fun Project.ensureJavadocJarTask(): Task =
    tasks.findByName(JAVADOC_JAR_TASK_NAME) ?: createJavadocJarTask()

private fun Project.createJavadocJarTask(): Task =
    tasks.create<Jar>(JAVADOC_JAR_TASK_NAME).apply {
        archiveClassifier.set("javadoc")
    }

private fun Project.applyMavenPublish() {
    plugins.apply("maven-publish")
}
