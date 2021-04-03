import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.geepawhill"
version = "1.0-SNAPSHOT"

plugins {
    application
    java
    kotlin("jvm") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

application {
    mainClassName = "org.geepawhill.gerrymander.MainKt"
}

kotlin {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "14"
        }
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

javafx {
    version = "15"
    modules = mutableListOf(
        "javafx.controls",
        "javafx.graphics",
        "javafx.fxml"
    )
}

repositories {
    maven {
        setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenCentral()
}

dependencies {
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
}