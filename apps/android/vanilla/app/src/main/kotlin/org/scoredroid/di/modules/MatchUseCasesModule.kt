package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.GetMatchFlowUseCase
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase
import org.scoredroid.usecase.CreateMatchUseCase

@Module
object MatchUseCasesModule {

    @Provides
    fun provideMatchEntrypoint(dataSource: PersistentMatchDataSource): MatchEntrypoint {
        return MatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideCreateMatchEntrypoint(dataSource: PersistentMatchDataSource): CreateMatchEntrypoint {
        return CreateMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideCreateMatchUseCase(entrypoint: CreateMatchEntrypoint): CreateMatchUseCase {
        return entrypoint.createMatchUseCase
    }

    @Provides
    fun provideGetMatchFlowUseCase(entrypoint: MatchEntrypoint): GetMatchFlowUseCase {
        return entrypoint.getMatchFlowUseCase
    }

    @Provides
    fun provideGetMatchesUseCase(entrypoint: MatchEntrypoint): GetMatchesUseCase {
        return entrypoint.getMatchesUseCase
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
