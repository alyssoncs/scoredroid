package org.scoredroid.creatematch.testdouble.data.datasource.local

import org.scoredroid.creatematch.data.datasource.local.MatchLocalDataSource
import org.scoredroid.creatematch.data.repository.AddTeamRepositoryRequest
import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.creatematch.domain.response.MatchResponse
import org.scoredroid.creatematch.domain.response.TeamResponse

class MatchLocalDataSourceStub : MatchLocalDataSource {

    var matchId: Long = 0

    override suspend fun createMatch(createMatchRequest: CreateMatchRequest): MatchResponse {
        return MatchResponse(
            id = matchId,
            teams = createMatchRequest.teams.map { TeamResponse("irrelevant") },
        )
    }

    override suspend fun addTeam(
        matchId: Long,
        team: AddTeamRepositoryRequest
    ): Result<MatchResponse> {
        TODO("Not yet implemented")
    }
}
