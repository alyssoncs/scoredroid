package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.DecrementScore
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.IncrementScore
import org.scoredroid.usecase.IncrementScoreUseCase

@Module
internal object PlayMatchUseCasesModule {
    @Provides
    fun provideIncrementScore(repository: MatchRepository): IncrementScoreUseCase {
        return IncrementScore(repository)
    }

    @Provides
    fun provideDecrementScore(repository: MatchRepository): DecrementScoreUseCase {
        return DecrementScore(repository)
    }
}
