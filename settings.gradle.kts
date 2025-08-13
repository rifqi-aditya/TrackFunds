pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "TrackFunds"
include(":app")
include(":core:ui")
include(":core:domain")
include(":core:navigation")
include(":core:data")
include(":feature:categories")
include(":feature:home")
include(":feature:account")
include(":feature:transaction")
include(":core:common")
include(":core:navigation:api")
include(":feature:budget")
include(":feature:scan")
include(":feature:reports")
include(":feature:savings")
include(":feature:auth")
include(":feature:onboarding")
