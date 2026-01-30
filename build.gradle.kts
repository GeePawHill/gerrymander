group = "org.geepawhill"
version = "1.0-SNAPSHOT"

plugins {
    application
    java
    kotlin("jvm") version "1.8.20"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

application {
    mainClass = "org.geepawhill.gerrymander.MainKt"
}

kotlin {
    jvmToolchain(17)
}
tasks.withType<Test> {
    useJUnitPlatform()
}

javafx {
    version = "17"
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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("com.google.guava:guava:30.1.1-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    implementation("com.googlecode.blaisemath.tornado:tornadofx-fx21k19:2.1.0")
    testImplementation("org.assertj:assertj-core:3.11.1")
}