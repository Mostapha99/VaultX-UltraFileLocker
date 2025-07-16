pluginManagement {
    repositories {
        google()
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

rootProject.name = "VaultX-UltraFileLocker"
include(":app")
include(":core")
include(":data")
include(":features:vault_gallery")
include(":features:media_player")
include(":features:security")
include(":features:ai")
include(":features:settings")
include(":plugins")

