plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "hyros-planet"
include ("core", "duels", "lib", "survival")
