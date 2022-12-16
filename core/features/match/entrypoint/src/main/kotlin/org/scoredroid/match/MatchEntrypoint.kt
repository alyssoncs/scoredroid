package org.scoredroid.match

import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.di.DaggerMatchComponent
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.match.domain.usecase.GetMatchFlowUseCase
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase

interface MatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): MatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val createMatchUseCase: CreateMatchUseCase
    val getMatchFlowUseCase: GetMatchFlowUseCase
    val getMatchesUseCase: GetMatchesUseCase
    val removeMatchUseCase: RemoveMatchUseCase
    val renameMatchUseCase: RenameMatchUseCase
    val saveMatchUseCase: SaveMatchUseCase
    val clearTransientMatchDataUseCase: ClearTransientMatchDataUseCase
}
