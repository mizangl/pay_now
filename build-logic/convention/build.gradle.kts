plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "io.mz.checkout.paynow.buildlogic"

// Configure the build-logic plugins to target JDK 17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    lintChecks(libs.androidx.lint.gradle)
    implementation(libs.truth)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("jvm") {
            id = libs.plugins.paynow.jvm.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.JvmLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.paynow.android.application.asProvider().get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidApplicationConventionPlugin"
        }

       register("androidApplicationCompose") {
            id = libs.plugins.paynow.android.application.compose.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = libs.plugins.paynow.android.library.asProvider().get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = libs.plugins.paynow.android.library.compose.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidLibraryComposeConventionPlugin"
        }

        register("hilt") {
            id = libs.plugins.paynow.hilt.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidHiltConventionPlugin"
        }

        register("androidFeature") {
            id = libs.plugins.paynow.android.feature.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidFeatureConventionPlugin"
        }

        register("androidLint") {
            id = libs.plugins.paynow.android.lint.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidLintConventionPlugin"
        }

        register("androidTest") {
            id = libs.plugins.paynow.android.test.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.AndroidTestConventionPlugin"
        }

        register("spotless") {
            id = libs.plugins.paynow.spotless.get().pluginId
            implementationClass = "io.mz.checkout.paynow.buildlogic.SpotlessConventionPlugin"
        }
    }
}
