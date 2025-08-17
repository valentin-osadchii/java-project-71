import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.51.0" // Актуальная версия на май 2025
    id("application")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = files(
        configurations.getByName("annotationProcessor")
    )
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("info.picocli:picocli:4.7.7")
    annotationProcessor("info.picocli:picocli-codegen:4.7.7")
    implementation("com.fasterxml.jackson.core:jackson-core:2.19.2") // Replace with the latest version
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.19.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2") // Replace with the latest version
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "hexlet.code.App"
}


tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    // Просто строка с разделителями-запятыми
    outputFormatter = "json,txt"

    // Булевы значения через прямое присваивание
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"

    // Остальная конфигурация
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview").any { qualifier ->
                    candidate.version.contains(qualifier, ignoreCase = true)
                }
                if (rejected) reject("Release candidate")
            }
        }
    }
}

