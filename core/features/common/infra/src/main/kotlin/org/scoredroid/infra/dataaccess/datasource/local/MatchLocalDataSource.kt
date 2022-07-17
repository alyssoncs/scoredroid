package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

interface MatchLocalDataSource {
    suspend fun createMatch(match: CreateMatchRepositoryRequest): Match
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match>
    suspend fun updateScoreTo(matchId: Long, teamAt: Int, newScore: Int): Result<Match>
    suspend fun getTeam(matchId: Long, teamAt: Int): Team?
}