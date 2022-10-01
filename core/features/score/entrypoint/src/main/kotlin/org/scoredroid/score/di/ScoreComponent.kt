package org.scoredroid.score.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.score.ScoreEntrypoint
import org.scoredroid.score.di.modules.ScoreUpdaterModule
import org.scoredroid.score.di.modules.ScoreUseCasesModule

@Component(
    modules = [
        ScoreUpdaterModule::class,
        ScoreUseCasesModule::class,
    ]
)
internal interface ScoreComponent : ScoreEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): ScoreComponent
    }
}
