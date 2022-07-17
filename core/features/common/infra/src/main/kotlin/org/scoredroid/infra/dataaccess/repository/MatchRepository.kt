package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {
    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
        return matchLocalDataSource.createMatch(createMatchRequest)
    }

    //TODO: create class for match id
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<MatchResponse> {
        return matchLocalDataSource.addTeam(matchId, team)
    }

    suspend fun incrementScoreBy(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        return matchLocalDataSource.incrementScoreBy(matchId, teamAt, increment)
    }

    suspend fun getTeam(matchId: Long, teamAt: Int): TeamResponse? {
        return matchLocalDataSource.getTeam(matchId, teamAt)
    }
}
