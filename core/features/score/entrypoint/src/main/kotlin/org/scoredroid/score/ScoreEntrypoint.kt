package org.scoredroid.score

import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.score.di.DaggerScoreComponent
import org.scoredroid.score.domain.usecase.DecrementScoreUseCase
import org.scoredroid.score.domain.usecase.IncrementScoreUseCase
import org.scoredroid.score.domain.usecase.ResetScoreUseCase

interface ScoreEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): ScoreEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerScoreComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val decrementScoreUseCase: DecrementScoreUseCase
    val incrementScoreUseCase: IncrementScoreUseCase
    val resetScoreUseCase: ResetScoreUseCase
}