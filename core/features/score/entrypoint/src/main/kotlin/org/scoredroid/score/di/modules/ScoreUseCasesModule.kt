package org.scoredroid.score.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.score.domain.usecase.DecrementScore
import org.scoredroid.score.domain.usecase.DecrementScoreUseCase
import org.scoredroid.score.domain.usecase.IncrementScore
import org.scoredroid.score.domain.usecase.IncrementScoreUseCase
import org.scoredroid.score.domain.usecase.ResetScore
import org.scoredroid.score.domain.usecase.ResetScoreUseCase
import org.scoredroid.score.domain.usecase.ScoreUpdater

@Module
internal object ScoreUseCasesModule {
    @Provides
    fun provideDecrementScore(scoreUpdater: ScoreUpdater): DecrementScoreUseCase {
        return DecrementScore(scoreUpdater)
    }

    @Provides
    fun provideIncrementScore(scoreUpdater: ScoreUpdater): IncrementScoreUseCase {
        return IncrementScore(scoreUpdater)
    }

    @Provides
    fun provideResetScore(scoreUpdater: ScoreUpdater): ResetScoreUseCase {
        return ResetScore(scoreUpdater)
    }
}
