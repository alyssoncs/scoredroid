package org.scoredroid.score.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.score.domain.usecase.ScoreUpdater

@Module
internal object ScoreUpdaterModule {
    @Provides
    fun provideScoreUpdater(repository: MatchRepository): ScoreUpdater {
        return ScoreUpdater(repository)
    }
}
