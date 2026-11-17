// Dónde Gradle busca los PLUGINS (Android, Kotlin, Compose).
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
// Dónde Gradle busca las LIBRERÍAS (Play Services, Maps Compose, etc.).
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "CampusGPS"
include(":app")
