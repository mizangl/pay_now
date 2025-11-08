package io.mz.checkout.paynow.buildlogic

import io.mz.checkout.paynow.buildlogic.task.configureKotlinJvm
import io.mz.checkout.paynow.buildlogic.task.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin ="org.jetbrains.kotlin.jvm")

            configureKotlinJvm()

            dependencies {
                "testImplementation"(findLibrary("kotlin.test"))
            }
        }
    }
}
