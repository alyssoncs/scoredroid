package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerPlayMatchComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.IncrementScoreUseCase

interface PlayMatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): PlayMatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerPlayMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val incrementScoreUseCase: IncrementScoreUseCase
    val decrementScoreUseCase: DecrementScoreUseCase
}
