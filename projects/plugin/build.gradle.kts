plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
}

buildConfig {
    className("KluaGradleBuildData")
    packageName("kofeychi.klua.gradle")

    val pluginProject = project(":projects:compiler")
    buildConfigField("String", "PLUGIN_GROUP", "\"${pluginProject.group}\"")
    buildConfigField("String", "PLUGIN_NAME", "\"${pluginProject.name}\"")
    buildConfigField("String", "PLUGIN_VERSION", "\"${pluginProject.version}\"")
}


kotlin {
    jvmToolchain(26)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}


gradlePlugin {
    plugins {
        create("klua") {
            id = rootProject.group.toString()
            implementationClass = "kofeychi.klua.gradle.KLuaGradlePlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}