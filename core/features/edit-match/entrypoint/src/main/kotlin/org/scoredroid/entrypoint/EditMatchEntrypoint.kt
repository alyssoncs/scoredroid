package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerEditMatchComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.AddTeamUseCase
import org.scoredroid.usecase.MoveTeamUseCase
import org.scoredroid.usecase.RemoveTeamUseCase
import org.scoredroid.usecase.RenameMatchUseCase
import org.scoredroid.usecase.RenameTeamUseCase

interface EditMatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): EditMatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerEditMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val renameMatchUseCase: RenameMatchUseCase
    val addTeamUseCase: AddTeamUseCase
    val moveTeamUseCase: MoveTeamUseCase
    val removeTeamUseCase: RemoveTeamUseCase
    val renameTeamUseCase: RenameTeamUseCase
}
