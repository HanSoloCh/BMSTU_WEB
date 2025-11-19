plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("plugin.serialization") version "1.9.0"  // Serialization plugin
    application
    idea
}

application {
    mainClass.set("com.example.app.ApplicationKt")
}

sourceSets {
    create("e2eTest") {
        kotlin.srcDir("src/e2eTest/kotlin")
    }
}

idea {
    module {
        sourceSets.named("e2eTest") {
            testSources.from(this.kotlin.srcDirs)
        }
    }
}

configurations.named("e2eTestImplementation") {
    extendsFrom(configurations["testImplementation"])
}
configurations.named("e2eTestRuntimeOnly") {
    extendsFrom(configurations["testRuntimeOnly"])
}


tasks.register<Test>("e2eTest") {
    description = "Runs E2E tests."
    group = "verification"
    testClassesDirs = sourceSets["e2eTest"].output.classesDirs
    classpath = sourceSets["e2eTest"].runtimeClasspath
    useJUnitPlatform()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.kotlinx.coroutines.core)

    // Для Postgres
    implementation(libs.postgresql)

    // Сериализация
    implementation(libs.ktor.serialization.kotlinx.json)

    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.ktor.server.status.pages)
    implementation(libs.ktor.server.cors) // Для CORS
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.java.jwt)
    implementation(libs.ktor.server.swagger)

    // Для Koin
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j) // Logger для Koin
    implementation(libs.logback.classic)

    // Exposed
    implementation(libs.exposed.core)
    // Connection pool
    implementation(libs.hikariCP)

    // Тестирование
    "e2eTestImplementation"(kotlin("test"))
    "e2eTestImplementation"(libs.ktor.server.test.host)

    // Тестовые контейнеры
    "e2eTestImplementation"(libs.postgresql)
    "e2eTestImplementation"(libs.testcontainers)
    "e2eTestImplementation"(libs.testcontainers.postgresql)

    // Клиент Ktor
    "e2eTestImplementation"(libs.ktor.client.core)
    "e2eTestImplementation"(libs.ktor.client.content.negotiation)
    "e2eTestImplementation"(libs.ktor.serialization.kotlinx.json)
    "e2eTestImplementation"(project(":api"))

    // JUnit 5
    "e2eTestImplementation"(libs.junit.jupiter.api)
    "e2eTestRuntimeOnly"(libs.junit.jupiter.engine)
}