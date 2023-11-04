package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase

@Module
object MatchUseCasesModule {

    @Provides
    fun provideMatchEntrypoint(dataSource: PersistentMatchDataSource): MatchEntrypoint {
        return MatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideRemoveMatchUseCase(entrypoint: MatchEntrypoint): RemoveMatchUseCase {
        return entrypoint.removeMatchUseCase
    }

    @Provides
    fun provideRenameMatchUseCase(entrypoint: MatchEntrypoint): RenameMatchUseCase {
        return entrypoint.renameMatchUseCase
    }

    @Provides
    fun provideSaveMatchUseCase(entrypoint: MatchEntrypoint): SaveMatchUseCase {
        return entrypoint.saveMatchUseCase
    }

    @Provides
    fun provideClearTransientMatchData(entrypoint: MatchEntrypoint): ClearTransientMatchDataUseCase {
        return entrypoint.clearTransientMatchDataUseCase
    }
}
