package io.mz.checkout.paynow.buildlogic

import com.android.build.gradle.LibraryExtension
import io.mz.checkout.paynow.buildlogic.configuration.Configuration
import io.mz.checkout.paynow.buildlogic.task.configureKotlinAndroid
import io.mz.checkout.paynow.buildlogic.task.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "paynow.android.lint")

            extensions.configure<LibraryExtension> {

                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Configuration.TARGET_VERSION
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true

                resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString("_")
                    .lowercase() + "_"
            }

            dependencies {
                "androidTestImplementation"(findLibrary("kotlin.test"))
                "testImplementation"(findLibrary("kotlin.test"))
            }
        }
    }
}
