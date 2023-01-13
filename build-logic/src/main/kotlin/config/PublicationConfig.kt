package config

data class PublicationConfig(
    val group: String,
    val remoteName: String,
    val license: Sequence<License> = sequenceOf(License()),
    val signing: Signing? = null,
    val credentials: RepositoryCredentials = RepositoryCredentials(),
) {

    val projectUrl: String = "https://github.com/rsicarelli/$remoteName"
    val scmUrl: String = "scm:git:git://github.com/rsicarelli/$remoteName"
    val mavenUrl: String = "https://maven.pkg.github.com/rsicarelli/$remoteName"

    data class License(
        val licenseName: String = "The Apache License, Version 2.0",
        val licenseUrl: String = "http://www.apache.org/licenses/LICENSE-2.0.txt",
    )

    data class Signing(
        val key: String = System.getenv("SIGNING_KEY"),
        val password: String = System.getenv("SIGNING_PASSWORD"),
    )

    data class RepositoryCredentials(
        val username: String? = System.getenv("REMOTE_USERNAME"),
        val password: String? = System.getenv("REMOTE_PASSWORD"),
    )
}
