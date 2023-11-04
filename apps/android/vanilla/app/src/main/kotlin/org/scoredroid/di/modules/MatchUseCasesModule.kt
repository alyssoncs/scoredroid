package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase
import org.scoredroid.usecase.CreateMatchUseCase
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.IncrementScoreUseCase

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
    fun provideGetMatchEntrypoint(dataSource: PersistentMatchDataSource): GetMatchEntrypoint {
        return GetMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun providePlayMatchEntrypoint(dataSource: PersistentMatchDataSource): PlayMatchEntrypoint {
        return PlayMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideCreateMatchUseCase(entrypoint: CreateMatchEntrypoint): CreateMatchUseCase {
        return entrypoint.createMatchUseCase
    }

    @Provides
    fun provideGetMatchFlowUseCase(entrypoint: GetMatchEntrypoint): GetMatchFlowUseCase {
        return entrypoint.getMatchFlowUseCase
    }

    @Provides
    fun provideGetMatchesFlowUseCase(entrypoint: GetMatchEntrypoint): GetMatchesFlowUseCase {
        return entrypoint.getMatchesFlowUseCase
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

    @Provides
    fun provideDecrementScoreUseCase(entrypoint: PlayMatchEntrypoint): DecrementScoreUseCase {
        return entrypoint.decrementScoreUseCase
    }

    @Provides
    fun provideIncrementScoreUseCase(entrypoint: PlayMatchEntrypoint): IncrementScoreUseCase {
        return entrypoint.incrementScoreUseCase
    }
}
