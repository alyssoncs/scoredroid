package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerRemoveMatchComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.RemoveMatchUseCase

interface RemoveMatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): RemoveMatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerRemoveMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val removeMatchUseCase: RemoveMatchUseCase
}
