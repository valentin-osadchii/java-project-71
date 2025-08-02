import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.51.0" // Актуальная версия на май 2025
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
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

