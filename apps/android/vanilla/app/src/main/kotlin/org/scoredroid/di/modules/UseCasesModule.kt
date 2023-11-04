package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.usecase.CreateMatchUseCase
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.IncrementScoreUseCase

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
}
