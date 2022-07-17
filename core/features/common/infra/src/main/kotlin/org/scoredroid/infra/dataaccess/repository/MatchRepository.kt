package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import java.lang.Integer.max

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
        update: (currentScore: Int) -> Int,
    ): Result<MatchResponse> {
        val currentScore = matchLocalDataSource.getTeam(matchId, teamAt)?.score ?: 0
        val updatedScore = max(update(currentScore), 0)
        return matchLocalDataSource.updateScoreTo(matchId, teamAt, updatedScore).map { it.toMatchResponse() }
    }

    companion object {
        fun Match.toMatchResponse() = MatchResponse(
            id = id,
            teams = teams.map { TeamResponse(it.name, it.score) }
        )
    }

}
