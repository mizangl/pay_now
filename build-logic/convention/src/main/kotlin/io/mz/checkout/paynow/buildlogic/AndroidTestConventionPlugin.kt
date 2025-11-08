package io.mz.checkout.paynow.buildlogic

import com.android.build.gradle.TestExtension
import io.mz.checkout.paynow.buildlogic.configuration.Configuration
import io.mz.checkout.paynow.buildlogic.task.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.test")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Configuration.TARGET_VERSION
            }
        }
    }
}
