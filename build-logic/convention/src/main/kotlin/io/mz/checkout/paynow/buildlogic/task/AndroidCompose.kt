package io.mz.checkout.paynow.buildlogic.task

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }

        dependencies {
            val bom = findLibrary("androidx-compose-bom")

            "implementation"(platform(bom))
            "implementation"(findLibrary("androidx-compose-ui-tooling-preview").get())
            "debugImplementation"(findLibrary("androidx-compose-ui-tooling").get())

            "androidTestImplementation"(platform(bom))
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        fun Provider<*>.relativeToRootProject(dir: String) = map {
            isolated.rootProject.projectDirectory.dir("build")
                .dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics").let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeReports").onlyIfTrue()
            .relativeToRootProject("compose-reports").let(metricsDestination::set)

        stabilityConfigurationFiles.add(isolated.rootProject.projectDirectory.file("compose_compiler-stability.conf"))

    }
}