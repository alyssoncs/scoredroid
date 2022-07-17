package org.scoredroid.infra.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class FakeMatchLocalDataSource : MatchLocalDataSource {
    private var currentId = 0L
    private val matches = mutableMapOf<Long, MatchResponse>()

    override suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
        val matchId = currentId++
        val matchResponse = MatchResponse(
            id = matchId,
            teams = createMatchRequest.teams.map { TeamResponse(it.toString(), score = 0) },
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
            val updatedMatch = match.copy(
                teams = match.teams + TeamResponse(name = team.name, score = 0)
            )
            matches[matchId] = updatedMatch
            return Result.success(updatedMatch)
        }

        return Result.failure(Throwable())
    }

    override suspend fun incrementScoreBy(
        matchId: Long,
        teamAt: Int,
        increment: Int
    ): Result<MatchResponse> {
        val match = matches[matchId] ?: return Result.failure(TeamOperationError.MatchNotFound)
        if (teamAt !in match.teams.indices) return Result.failure(TeamOperationError.TeamNotFound)

        val updatedMatch = match.incrementScore(teamAt, increment)
        matches[matchId] = updatedMatch
        return Result.success(updatedMatch)
    }

    override suspend fun getTeam(matchId: Long, teamAt: Int): TeamResponse? {
        return matches[matchId]?.teams?.getOrNull(teamAt)
    }

    private fun MatchResponse.incrementScore(
        teamAt: Int,
        increment: Int
    ): MatchResponse {
        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt) {
                    team.copy(score = team.score + increment)
                } else {
                    team
                }
            }
        )
    }
}
