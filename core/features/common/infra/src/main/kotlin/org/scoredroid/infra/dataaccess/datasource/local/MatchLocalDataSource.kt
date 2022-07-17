package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest

interface MatchLocalDataSource {
    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<MatchResponse>
    suspend fun incrementScoreBy(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse>
    suspend fun getTeam(matchId: Long, teamAt: Int): TeamResponse?
}