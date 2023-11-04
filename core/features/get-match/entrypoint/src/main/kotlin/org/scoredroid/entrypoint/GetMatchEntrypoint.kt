package org.scoredroid.entrypoint

import org.scoredroid.entrypoint.di.DaggerGetMatchComponent
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.GetMatchesFlowUseCase

interface GetMatchEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): GetMatchEntrypoint {
            val infraEntrypoint = InfraEntrypoint.create(persistentMatchDataSource)
            return DaggerGetMatchComponent.factory().create(infraEntrypoint.matchRepository)
        }
    }

    val getMatchFlowUseCase: GetMatchFlowUseCase
    val getMatchesFlowUseCase: GetMatchesFlowUseCase
}
