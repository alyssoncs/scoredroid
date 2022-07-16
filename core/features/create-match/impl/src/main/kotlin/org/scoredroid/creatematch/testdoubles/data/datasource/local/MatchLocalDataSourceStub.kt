package org.scoredroid.creatematch.testdoubles.data.datasource.local

import org.scoredroid.creatematch.data.datasource.local.MatchLocalDataSource
import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.creatematch.domain.response.MatchResponse
import org.scoredroid.creatematch.domain.response.TeamResponse

class MatchLocalDataSourceStub : MatchLocalDataSource {

    var matchId: Long = 0

    override suspend fun createMatch(createMatchRequest: CreateMatchRequest): MatchResponse {
        return MatchResponse(
            id = matchId,
            teams = createMatchRequest.teams.map { TeamResponse() },
        )
    }
}
