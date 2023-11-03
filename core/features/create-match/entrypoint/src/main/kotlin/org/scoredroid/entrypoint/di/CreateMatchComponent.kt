package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.di.modules.CreateMatchUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        CreateMatchUseCasesModule::class,
    ],
)
internal interface CreateMatchComponent : CreateMatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): CreateMatchComponent
    }
}
