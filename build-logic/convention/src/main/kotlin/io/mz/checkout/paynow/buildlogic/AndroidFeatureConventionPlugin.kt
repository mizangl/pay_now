package io.mz.checkout.paynow.buildlogic

import com.android.build.gradle.LibraryExtension
import io.mz.checkout.paynow.buildlogic.task.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            apply(plugin ="paynow.hilt")
            apply(plugin ="paynow.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {

                "implementation"(findLibrary("androidx.hilt.navigation.compose"))
                "implementation"(findLibrary("androidx.lifecycle.runtime.compose"))
                "implementation"(findLibrary("androidx.lifecycle.viewmodel.compose"))
                "implementation"(findLibrary("androidx.navigation.compose"))
                "implementation"(findLibrary("androidx.tracing.ktx"))
                "implementation"(findLibrary("kotlinx.serialization.json"))

                "testImplementation"(findLibrary("androidx.navigation.testing"))
                "androidTestImplementation"(findLibrary("androidx.lifecycle.runtime.testing"))
            }
        }
    }
}
