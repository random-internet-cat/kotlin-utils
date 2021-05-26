plugins {
    kotlin("jvm") version "1.4.30"
    `maven-publish`
    signing
}

group = "org.randomcat"
version = "2.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.4")

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

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        val main by creating(MavenPublication::class) {
            from(components["java"])

            pom {
                name.set("kotlin-utils")
                description.set("A collection of utilities for the Kotlin programming language.")
                url.set("https://github.com/randomnetcat/kotlin-utils/")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("randomnetcat")
                        name.set("Jason Cobb")
                        email.set("dev@randomcat.org")
                    }
                }

                scm {
                    connection.set("scm:git:git@github.com:randomnetcat/kotlin-utils.git")
                    developerConnection.set("scm:git:git@github.com:randomnetcat/kotlin-utils.git")
                    url.set("https://github.com/randomnetcat/kotlin-utils/")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")

            credentials {
                val ossrhUsername: String? by project
                val ossrhPassword: String? by project

                username = ossrhUsername ?: return@credentials
                password = ossrhPassword ?: return@credentials
            }
        }
    }
}

signing {
    run {
        val key = System.getenv("SIGNING_KEY") ?: return@run
        val password = System.getenv("SIGNING_PASSWORD") ?: return@run

        useInMemoryPgpKeys(key, password)
    }

    sign(publishing.publications)
}
