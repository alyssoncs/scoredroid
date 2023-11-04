package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.EditMatchEntrypoint
import org.scoredroid.entrypoint.di.modules.EditMatchUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        EditMatchUseCasesModule::class,
    ],
)
internal interface EditMatchComponent : EditMatchEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): EditMatchComponent
    }
}
