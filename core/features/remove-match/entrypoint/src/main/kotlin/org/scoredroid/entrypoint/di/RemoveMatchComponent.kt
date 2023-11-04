package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.RemoveMatchEntrypoint
import org.scoredroid.entrypoint.di.modules.RemoveMatchUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        RemoveMatchUseCasesModule::class,
    ],
)
internal interface RemoveMatchComponent : RemoveMatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): RemoveMatchComponent
    }
}
