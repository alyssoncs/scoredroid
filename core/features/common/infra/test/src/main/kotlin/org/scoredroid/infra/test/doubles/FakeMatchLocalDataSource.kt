package org.scoredroid.infra.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class FakeMatchLocalDataSource : MatchLocalDataSource {
    private var currentId = 0L
    private val matches = mutableMapOf<Long, MatchResponse>()

    override suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
        val matchId = currentId++
        val matchResponse = MatchResponse(
            id = matchId,
            teams = createMatchRequest.teams.map { TeamResponse(it.toString()) },
        )
        matches[matchId] = matchResponse

        return matchResponse
    }

    override suspend fun addTeam(
        matchId: Long,
        team: AddTeamRepositoryRequest
    ): Result<MatchResponse> {
        val match = matches[matchId]

        if (match != null) {
            return Result.success(
                match.copy(
                    teams = match.teams + TeamResponse(name = team.name)
                )
            )
        }

        return Result.failure(Throwable())
    }
}
