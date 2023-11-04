package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.domain.usecase.RenameMatchUseCase

@Module
object MatchUseCasesModule {

    @Provides
    fun provideMatchEntrypoint(dataSource: PersistentMatchDataSource): MatchEntrypoint {
        return MatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideRenameMatchUseCase(entrypoint: MatchEntrypoint): RenameMatchUseCase {
        return entrypoint.renameMatchUseCase
    }
}
