plugins {
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
}

gradlePlugin {
    plugins {
        register("klua") {
            version = "1.0"
            id = "kofeychi.klua_plugin"
            implementationClass = "kofeychi.klua.gradle.KLuaGradlePlugin"
        }
    }
}
publishing {
    repositories {
        mavenLocal()
    }
}