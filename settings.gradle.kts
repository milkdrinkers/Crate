pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

rootProject.name = "Crate"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "api",
    "yaml",
    "json",
    "toml"
)
