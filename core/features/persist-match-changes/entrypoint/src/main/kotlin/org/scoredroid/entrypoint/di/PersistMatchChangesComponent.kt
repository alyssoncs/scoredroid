package org.scoredroid.entrypoint.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.entrypoint.PersistMatchChangesEntrypoint
import org.scoredroid.entrypoint.di.modules.PersistMatchChangesUseCasesModule
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Component(
    modules = [
        PersistMatchChangesUseCasesModule::class,
    ],
)
internal interface PersistMatchChangesComponent : PersistMatchChangesEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance matchRepository: MatchRepository,
        ): PersistMatchChangesComponent
    }
}
