plugins {
    kotlin("jvm") version "1.9.0"
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.allure)
}

subprojects {
    apply(plugin = "io.qameta.allure")

    tasks.register<Delete>("cleanAllureReport") {
        delete(layout.buildDirectory.dir("reports/allure-report/allureReport"))
    }

    tasks.register<Copy>("copyAllureHistory") {
        from(layout.buildDirectory.dir("reports/allure-report/allureReport/history"))
        into(layout.buildDirectory.dir("allure-results/history"))
        doFirst {
            println("[${project.name}] Copying Allure history from last report...")
        }
    }

    tasks.withType<Test>().configureEach {
        dependsOn("copyAllureHistory")
    }

    tasks.named("allureReport") {
        dependsOn("cleanAllureReport")
        dependsOn(tasks.withType<Test>())
    }
}
