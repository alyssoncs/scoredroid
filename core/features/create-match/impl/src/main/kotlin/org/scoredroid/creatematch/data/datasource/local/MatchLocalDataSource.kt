package org.scoredroid.creatematch.data.datasource.local

import org.scoredroid.creatematch.data.repository.AddTeamRepositoryRequest
import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.data.response.MatchResponse

interface MatchLocalDataSource {
    suspend fun createMatch(createMatchRequest: CreateMatchRequest): MatchResponse
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<MatchResponse>
}