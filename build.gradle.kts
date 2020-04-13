plugins {
    kotlin("jvm") version "1.3.71"
}

// Must always be updated along with plugins block
val kotlinVersion = "1.3.71"

group = "io.github.random-internet-cat"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
