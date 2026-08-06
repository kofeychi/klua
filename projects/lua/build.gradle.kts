plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(26)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}