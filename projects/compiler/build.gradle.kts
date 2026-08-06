plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    id("idea")
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    fileTree("lib").forEach {
        implementation(files(it))
    }
    implementation(kotlin("reflect"))
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

buildConfig {
    className("KluaCompilerBuildData")
    packageName("kofeychi.klua.compiler")

    buildConfigField("String","PLUGIN_ID","\"klua\"")
}

kotlin {
    jvmToolchain(26)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}