import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.gradleup.shadow") version "8.3.0"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://libraries.minecraft.net")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":lib"))
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("HyroDuels-${version}.jar")
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

artifacts {
    archives(tasks.named<ShadowJar>("shadowJar"))
}