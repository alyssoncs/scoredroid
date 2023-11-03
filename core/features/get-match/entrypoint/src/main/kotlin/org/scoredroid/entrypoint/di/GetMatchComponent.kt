package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.di.modules.GetMatchUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        GetMatchUseCasesModule::class,
    ],
)
internal interface GetMatchComponent : GetMatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): GetMatchComponent
    }
}
