import kotlin.String

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val it_unibo_alchemist: String = "9.3.0"

    const val org_jetbrains_kotlin: String = "1.3.61"

    const val de_fayard_refreshversions_gradle_plugin: String = "0.7.0" // available: "0.8.6"

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.61"

    /**
     * Current version: "6.0.1"
     * See issue 19: How to update Gradle itself?
     * https://github.com/jmfayard/buildSrcVersions/issues/19
     */
    const val gradleLatestVersion: String = "6.0.1"
}
