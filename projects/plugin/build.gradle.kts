plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
}

kotlin {
    jvmToolchain(26)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}