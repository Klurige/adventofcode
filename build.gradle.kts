import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.wrapper {
    gradleVersion="7.6"
}

plugins {
    kotlin("jvm") version "1.7.21"
    application
    kotlin("plugin.serialization") version "1.7.20"
}

group = "loc.cpac"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-cio:2.2.1")
    implementation("io.ktor:ktor-client-logging:2.2.1")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")}
