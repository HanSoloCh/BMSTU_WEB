plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"  // Serialization plugin
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(libs.kotlinx.coroutines.core)

    // Сериализация
    implementation(libs.ktor.serialization.kotlinx.json)

    // Tests
    testImplementation(project(":data"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.logback.classic)

    // Exposed
    testImplementation(libs.exposed.core)
    testImplementation(libs.exposed.dao)
    testImplementation(libs.exposed.jdbc)
    testImplementation(libs.exposed.kotlin.datetime)
    testImplementation(libs.sqlite.jdbc)

    // Тестовые контейнеры
    testImplementation(libs.postgresql)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.postgresql)

    // allure
    testImplementation(libs.allure.junit5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}