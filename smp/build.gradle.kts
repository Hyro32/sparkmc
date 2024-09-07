plugins {
    id("java")
}

group = "one.hyro.spark.smp"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://mvn.exceptionflug.de/repository/exceptionflug-public/")
    maven("https://repo.md-5.net/content/groups/public/")
    maven("https://ci.pluginwiki.us/plugin/repository/everything/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation("net.dv8tion:JDA:5.0.0-beta.24")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("net.kyori:adventure-api:4.17.0")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("net.md-5:bungeecord-api:1.20-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    archiveBaseName.set("SparkSmp")
}
