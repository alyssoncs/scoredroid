package org.scoredroid.match

import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.di.DaggerMatchComponent
import org.scoredroid.match.domain.usecase.RenameMatchUseCase

interface MatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): MatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val renameMatchUseCase: RenameMatchUseCase
}
