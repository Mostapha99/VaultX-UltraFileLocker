plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.1" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
}

subprojects {
    // نطبق detekt و ktlint على جميع الموديولات فقط
    afterEvaluate {
        plugins.withId("io.gitlab.arturbosch.detekt") {
            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
                config = files("$rootDir/config/detekt/detekt.yml")
                buildUponDefaultConfig = true
            }
        }

        plugins.withId("org.jlleitschuh.gradle.ktlint") {
            extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
                version.set("0.50.0")
                debug.set(true)
                verbose.set(true)
                android.set(false)
                outputToConsole.set(true)
                outputColorName.set("RED")
                ignoreFailures.set(false)
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
