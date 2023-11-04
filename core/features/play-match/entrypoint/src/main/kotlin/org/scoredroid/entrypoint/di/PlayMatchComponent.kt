package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.entrypoint.di.modules.PlayMatchUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        PlayMatchUseCasesModule::class,
    ],
)
internal interface PlayMatchComponent : PlayMatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): PlayMatchComponent
    }
}
