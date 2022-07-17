package org.scoredroid.infra.dataaccess.repository

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import java.lang.Integer.max

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {
    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): MatchResponse {
        val response = matchLocalDataSource.createMatch(createMatchRequest)
        return response.toMatchResponse()
    }

    //TODO: create class for match id
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<MatchResponse> {
        val addTeam = matchLocalDataSource.addTeam(matchId, team)
        return addTeam.map { it.toMatchResponse() }
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

    private fun Match.toMatchResponse() = MatchResponse(
        id = id,
        teams = teams.map { TeamResponse(it.name, it.score) }
    )

}
