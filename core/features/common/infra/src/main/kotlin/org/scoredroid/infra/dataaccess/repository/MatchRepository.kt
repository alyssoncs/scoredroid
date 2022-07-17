package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.orZero
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
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
}
