plugins {
    alias(libs.plugins.kotlin.jvm)
}


group = (property("group") as String)+".lua"
version = property("version") as String

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(26)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}