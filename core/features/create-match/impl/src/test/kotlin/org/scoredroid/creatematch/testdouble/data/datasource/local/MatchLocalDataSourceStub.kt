package org.scoredroid.creatematch.testdouble.data.datasource.local

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

//TODO: move to another module, or delete
class MatchLocalDataSourceStub : MatchLocalDataSource {

    var matchId: Long = 0

    override suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
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
