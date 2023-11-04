package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerPersistMatchChangesComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.usecase.SaveMatchUseCase

interface PersistMatchChangesEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): PersistMatchChangesEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerPersistMatchChangesComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val saveMatchUseCase: SaveMatchUseCase
    val clearTransientMatchDataUseCase: ClearTransientMatchDataUseCase
}
