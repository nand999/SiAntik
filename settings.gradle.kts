pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven{
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
//        mavenCentral()
        maven{
            url = uri("https://jitpack.io")
        }
        jcenter()
    }
}

rootProject.name = "SiAntik"
include(":app")
