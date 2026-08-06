dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://jetbrains.space")
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


rootProject.name = "klua"

include(":projects:lua")
include(":projects:compiler")
include(":projects:plugin")