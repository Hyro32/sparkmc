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
    implementation("com.jeff-media:custom-block-data:2.2.2")
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
}