package org.scoredroid.teams.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.teams.TeamsEntrypoint
import org.scoredroid.teams.di.modules.TeamsUseCasesModule

@Component(
    modules = [
        TeamsUseCasesModule::class,
    ],
)
internal interface TeamsComponent : TeamsEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): TeamsComponent
    }
}
