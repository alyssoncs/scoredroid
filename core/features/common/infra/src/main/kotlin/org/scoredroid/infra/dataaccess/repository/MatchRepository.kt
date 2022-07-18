package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.orZero
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {
    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
        return matchLocalDataSource.createMatch(createMatchRequest)
    }

    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return matchLocalDataSource.addTeam(matchId, team)
    }

    suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return matchLocalDataSource.removeTeam(matchId, teamAt)
    }

    suspend fun updateScore(
        matchId: Long,
        teamAt: Int,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val currentScore = matchLocalDataSource.getTeam(matchId, teamAt)?.score.orZero()
        return matchLocalDataSource.updateScoreTo(matchId, teamAt, update(currentScore))
    }

    suspend fun updateScoreForAllTeams(
        matchId: Long,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val match = matchLocalDataSource.getMatch(matchId)
        return if (match != null) {
            if (match.teams.isEmpty())
                Result.success(match)
            else {
                updateScoreForAllTeams(match, matchId, update)
            }
        } else {
            Result.failure(TeamOperationError.MatchNotFound)
        }
    }

    private suspend fun updateScoreForAllTeams(
        match: Match,
        matchId: Long,
        update: (currentScore: Score) -> Score
    ): Result<Match> {
        var result: Result<Match> = Result.failure(TeamOperationError.MatchNotFound)
        match.teams.forEachIndexed { teamIdx, team ->
            result = matchLocalDataSource.updateScoreTo(matchId, teamIdx, update(team.score))
        }
        return result
    }

    suspend fun renameMatch(matchId: Long, name: String): Result<Match> {

        return matchLocalDataSource.renameMatch(matchId, name)
    }
}
