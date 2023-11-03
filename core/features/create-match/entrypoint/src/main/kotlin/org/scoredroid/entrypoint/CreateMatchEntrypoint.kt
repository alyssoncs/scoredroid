package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerCreateMatchComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.CreateMatchUseCase

interface CreateMatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): CreateMatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerCreateMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val createMatchUseCase: CreateMatchUseCase
}
