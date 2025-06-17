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
include(":feature:add_transaction")
include(":feature:account")
include(":feature:profile")
include(":feature:transaction")
include(":core:common")
include(":core:navigation:api")
