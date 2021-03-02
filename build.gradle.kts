plugins {
    kotlin("jvm") version "1.4.30"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5"
}

group = "io.github.random-internet-cat"
version = "2.0.0-SNAPSHOT"

val bintrayRepo = "kotlin-utils"

val artifactName = project.name
val artifactGroup = project.group.toString()
val artifactVersion = project.version.toString()

val githubUrl = "https://github.com/random-internet-cat/kotlin-utils"
val licenseName = "MIT"

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val mavenPublicationName = "maven-publication"

publishing {
    publications {
        create<MavenPublication>(mavenPublicationName) {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
            from(components["kotlin"])

            artifact(sourcesJar)
        }
    }
}

bintray {
    user = project.findProperty("bintrayUser").toString()
    key = project.findProperty("bintrayKey").toString()

    pkg.apply {
        repo = bintrayRepo
        name = artifactName
        vcsUrl = githubUrl

        setLicenses(licenseName)
        setPublications(mavenPublicationName)

        version.apply {
            name = artifactVersion
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
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
