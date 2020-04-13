plugins {
    kotlin("jvm") version "1.3.71"
}

// Must always be updated along with plugins block
val kotlinVersion = "1.3.71"

group = "io.github.random-internet-cat"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    testImplementation(kotlin("test", kotlinVersion))
    testImplementation(kotlin("test-junit5", kotlinVersion))
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.6.1")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
