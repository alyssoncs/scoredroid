package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {
    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
        return matchLocalDataSource.createMatch(createMatchRequest)
    }

    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<MatchResponse> {
        return matchLocalDataSource.addTeam(matchId, team)
    }
}
