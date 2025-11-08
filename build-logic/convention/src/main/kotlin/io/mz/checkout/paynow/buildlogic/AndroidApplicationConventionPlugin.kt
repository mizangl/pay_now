package io.mz.checkout.paynow.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import io.mz.checkout.paynow.buildlogic.configuration.Configuration
import io.mz.checkout.paynow.buildlogic.task.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            apply(plugin ="com.android.application")
            apply(plugin ="org.jetbrains.kotlin.android")
            apply(plugin ="paynow.android.lint")

            extensions.configure<ApplicationExtension> {

                configureKotlinAndroid(this)
                compileSdk {
                    version = release(Configuration.COMPILE_VERSION)
                }

                defaultConfig.minSdk = Configuration.MIN_VERSION
                defaultConfig.targetSdk = Configuration.TARGET_VERSION
                defaultConfig.versionCode = Configuration.BUILD_NUMBER
                defaultConfig.versionName = Configuration.VERSION
                testOptions.animationsDisabled = true
            }

        }
    }
}
