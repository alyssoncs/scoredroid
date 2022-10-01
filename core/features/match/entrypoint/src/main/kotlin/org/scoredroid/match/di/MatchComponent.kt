package org.scoredroid.match.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.di.modules.MatchUseCasesModule

@Component(
    modules = [
        MatchUseCasesModule::class,
    ]
)
internal interface MatchComponent : MatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): MatchComponent
    }
}
