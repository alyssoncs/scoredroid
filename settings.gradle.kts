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
include(":apps:android:common:infra:dependencies:database")
include(":apps:android:common:infra:dependencies:impl")
include(":apps:android:common:utils:fragments:factory-annotation")
include(":apps:android:common:utils:fragments:fragment-factory")
include(":apps:android:common:utils:fragments:transactions")
include(":apps:android:common:utils:viewmodels:factory-annotation")
include(":apps:android:common:utils:viewmodels:test")
include(":apps:android:common:utils:viewmodels:vm-factory")
include(":apps:android:vanilla:app")
include(":apps:android:vanilla:common:ui:components")
include(":apps:android:vanilla:common:ui:theme")
include(":apps:android:vanilla:common:ui:tooling")
include(":apps:android:vanilla:features:create-match:public")
include(":apps:android:vanilla:features:create-match:impl")
include(":apps:android:vanilla:features:edit-match:public")
include(":apps:android:vanilla:features:edit-match:impl")
include(":apps:android:vanilla:features:history:impl")
include(":apps:android:vanilla:features:play:public")
include(":apps:android:vanilla:features:play:impl")
include(":core:domain")
include(":core:features:common:data")
include(":core:features:common:utils")
include(":core:features:match:entrypoint")
include(":core:features:match:public")
include(":core:features:match:impl")
include(":core:features:score:entrypoint")
include(":core:features:score:public")
include(":core:features:score:impl")
include(":core:features:teams:entrypoint")
include(":core:features:teams:public")
include(":core:features:teams:impl")
include(":core:infra:dependencies:public")
include(":core:infra:dependencies:impl")
include(":core:infra:entrypoint")
include(":core:infra:public")
