import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    id("idea")
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = property("group") as String
    version = property("version") as String

    repositories {
        mavenCentral()
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain(26)
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}