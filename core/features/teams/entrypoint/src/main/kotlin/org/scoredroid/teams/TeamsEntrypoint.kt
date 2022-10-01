package org.scoredroid.teams

import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.teams.di.DaggerTeamsComponent
import org.scoredroid.teams.domain.usecase.AddTeamUseCase
import org.scoredroid.teams.domain.usecase.MoveTeamUseCase
import org.scoredroid.teams.domain.usecase.RemoveTeamUseCase

interface TeamsEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): TeamsEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerTeamsComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val addTeamUseCase: AddTeamUseCase
    val moveTeamUseCase: MoveTeamUseCase
    val removeTeamUseCase: RemoveTeamUseCase
}