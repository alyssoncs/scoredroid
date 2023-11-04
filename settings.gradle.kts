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
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "scoredroid"
include(":apps:android:common:infra:dependencies:database")
include(":apps:android:common:infra:dependencies:impl")
include(":apps:android:common:utils:fragments:compose")
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
include(":core:entities")
include(":core:features:common:data")
include(":core:features:common:utils")
include(":core:features:create-match:entrypoint")
include(":core:features:create-match:public")
include(":core:features:create-match:impl")
include(":core:features:edit-match:entrypoint")
include(":core:features:edit-match:public")
include(":core:features:edit-match:impl")
include(":core:features:get-match:entrypoint")
include(":core:features:get-match:public")
include(":core:features:get-match:impl")
include(":core:features:persist-match-changes:entrypoint")
include(":core:features:persist-match-changes:public")
include(":core:features:persist-match-changes:impl")
include(":core:features:play-match:entrypoint")
include(":core:features:play-match:public")
include(":core:features:play-match:impl")
include(":core:features:remove-match:entrypoint")
include(":core:features:remove-match:public")
include(":core:features:remove-match:impl")
include(":core:infra:entrypoint")
include(":core:infra:match-repository:data-sources:impl")
include(":core:infra:match-repository:data-sources:public")
include(":core:infra:match-repository:entrypoint")
include(":core:infra:match-repository:public")
