package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.PersistMatchChangesEntrypoint
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.entrypoint.RemoveMatchEntrypoint
import org.scoredroid.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.usecase.CreateMatchUseCase
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.IncrementScoreUseCase
import org.scoredroid.usecase.RemoveMatchUseCase
import org.scoredroid.usecase.SaveMatchUseCase

@Module(
    includes = [
        FeatureEntrypointsModule::class,
        MatchUseCasesModule::class,
        TeamsUseCasesModule::class,
    ],
)
object UseCasesModule {

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
    fun provideDecrementScoreUseCase(entrypoint: PlayMatchEntrypoint): DecrementScoreUseCase {
        return entrypoint.decrementScoreUseCase
    }

    @Provides
    fun provideIncrementScoreUseCase(entrypoint: PlayMatchEntrypoint): IncrementScoreUseCase {
        return entrypoint.incrementScoreUseCase
    }

    @Provides
    fun provideRemoveMatchUseCase(entrypoint: RemoveMatchEntrypoint): RemoveMatchUseCase {
        return entrypoint.removeMatchUseCase
    }

    @Provides
    fun provideSaveMatchUseCase(entrypoint: PersistMatchChangesEntrypoint): SaveMatchUseCase {
        return entrypoint.saveMatchUseCase
    }

    @Provides
    fun provideClearTransientMatchData(entrypoint: PersistMatchChangesEntrypoint): ClearTransientMatchDataUseCase {
        return entrypoint.clearTransientMatchDataUseCase
    }
}
