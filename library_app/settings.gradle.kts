pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven { url = uri("https://repo.jetbrains.com/kotlin") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven { url = uri("https://repo.jetbrains.com/kotlin") }
    }
}

rootProject.name = "library_app"
include(":domain", ":data", ":api")
