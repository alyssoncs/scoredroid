package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.Score.Companion.toScore
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

    //TODO: create class for match id
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return matchLocalDataSource.addTeam(matchId, team)
    }

    suspend fun updateScore(
        matchId: Long,
        teamAt: Int,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val currentScore = matchLocalDataSource.getTeam(matchId, teamAt)?.score.orZero()
        return matchLocalDataSource.updateScoreTo(matchId, teamAt, update(currentScore))
    }

    suspend fun resetScore(
        matchId: Long,
    ): Result<Match> {
        val match = matchLocalDataSource.getMatch(matchId)
        return if (match != null) {
            if (match.teams.isEmpty())
                Result.success(match)
            else
                resetScore(match, matchId)
        } else {
            Result.failure(TeamOperationError.MatchNotFound)
        }
    }

    private suspend fun resetScore(
        match: Match,
        matchId: Long
    ): Result<Match> {
        var result: Result<Match> = Result.failure(TeamOperationError.MatchNotFound)
        for (teamIdx in match.teams.indices) {
            result = matchLocalDataSource.updateScoreTo(matchId, teamIdx, 0.toScore())
        }
        return result
    }
}
