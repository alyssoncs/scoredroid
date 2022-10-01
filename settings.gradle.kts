pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "scoredroid"
include(":app")
include(":core:features:common:data")
include(":core:features:common:domain")
include(":core:features:common:infra:commonmodels")
include(":core:features:common:infra:concretedependencies")
include(":core:features:common:infra:dependencies")
include(":core:features:common:infra:entrypoint")
include(":core:features:common:infra:public")
include(":core:features:common:infra:test")
include(":core:features:common:utils")
include(":core:features:match:public")
include(":core:features:match:impl")
include(":core:features:score:public")
include(":core:features:score:impl")
include(":core:features:teams:public")
include(":core:features:teams:impl")
