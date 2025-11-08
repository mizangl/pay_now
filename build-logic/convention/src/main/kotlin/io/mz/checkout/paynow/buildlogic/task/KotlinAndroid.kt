package io.mz.checkout.paynow.buildlogic.task

import com.android.build.api.dsl.CommonExtension
import io.mz.checkout.paynow.buildlogic.configuration.Configuration
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk {
            version = release(Configuration.COMPILE_VERSION)
        }

        defaultConfig {
            minSdk = Configuration.MIN_VERSION

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
                isCoreLibraryDesugaringEnabled = true
            }
        }

        lint {
            abortOnError = false
        }

        configureKotlin<KotlinAndroidProjectExtension>()

        dependencies {
            "coreLibraryDesugaring"(findLibrary("android.desugar.jdk.libs"))
        }
    }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {

    val warningsAsError: String? by project


    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions

        else -> throw UnsupportedOperationException("Unsupported Kotlin base extension $this ${T::class}")
    }.apply {

        jvmTarget.set(JvmTarget.JVM_17)
        allWarningsAsErrors.set(warningsAsError.toBoolean())

        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
        freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
        freeCompilerArgs.add("-opt-in=DateTimeFormat.formatAsKotlinBuilderDsl")
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        freeCompilerArgs.add("-opt-in=kotlinx.datetime.format.FormatStringsInDatetimeFormats")
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
        freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
        freeCompilerArgs.add("-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi")
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}