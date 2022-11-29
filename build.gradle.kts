import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.wrapper {
    gradleVersion="7.6"
}

plugins {
    kotlin("jvm") version "1.7.21"
    application
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
}