package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.score.ScoreEntrypoint
import org.scoredroid.score.domain.usecase.DecrementScoreUseCase
import org.scoredroid.score.domain.usecase.IncrementScoreUseCase
import org.scoredroid.score.domain.usecase.ResetScoreUseCase

@Module
object ScoreUseCasesModule {

    @Provides
    fun provideScoreEntrypoint(dataSource: PersistentMatchDataSource): ScoreEntrypoint {
        return ScoreEntrypoint.create(dataSource)
    }

    @Provides
    fun provideDecrementScoreUseCase(entrypoint: ScoreEntrypoint): DecrementScoreUseCase {
        return entrypoint.decrementScoreUseCase
    }

    @Provides
    fun provideIncrementScoreUseCase(entrypoint: ScoreEntrypoint): IncrementScoreUseCase {
        return entrypoint.incrementScoreUseCase
    }

    @Provides
    fun provideResetScoreUseCase(entrypoint: ScoreEntrypoint): ResetScoreUseCase {
        return entrypoint.resetScoreUseCase
    }
}
