package org.scoredroid.infra.test.doubles

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class FakeMatchLocalDataSource : MatchLocalDataSource {
    private var currentId = 0L
    private val matches = mutableMapOf<Long, Match>()

    override suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match {
        val matchId = currentId++
        val match = Match(
            id = matchId,
            name = matchRequest.name,
            teams = matchRequest.teams.map { Team(name = it.name, score = 0.toScore()) },
        )

        matches[matchId] = match
        return match
    }

    override suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(
                teams = match.teams + Team(name = team.name, score = 0.toScore())
            )
        }
    }

    override suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(
                teams = match.teams.filterIndexed { idx, _ -> idx != teamAt }
            )
        }
    }

    private fun updateMatch(
        matchId: Long,
        update: (Match) -> Match
    ): Result<Match> {
        val match = matches[matchId]

        if (match != null) {
            val updatedMatch = update(match)

            matches[matchId] = updatedMatch
            return Result.success(updatedMatch)
        }

        return Result.failure(Throwable())
    }

    override suspend fun updateScoreTo(
        matchId: Long,
        teamAt: Int,
        newScore: Score
    ): Result<Match> {
        val match = matches[matchId] ?: return Result.failure(TeamOperationError.MatchNotFound)
        if (teamAt !in match.teams.indices) return Result.failure(TeamOperationError.TeamNotFound)

        val updatedMatch = match.updateScore(teamAt, newScore)
        matches[matchId] = updatedMatch
        return Result.success(updatedMatch)
    }

    override suspend fun getMatch(matchId: Long): Match? {
        return matches[matchId]
    }

    override suspend fun getTeam(matchId: Long, teamAt: Int): Team? {
        return matches[matchId]?.teams?.getOrNull(teamAt)
    }

    private fun Match.updateScore(
        teamAt: Int,
        newScore: Score
    ): Match {
        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt) {
                    team.copy(score = newScore)
                } else {
                    team
                }
            }
        )
    }
}
