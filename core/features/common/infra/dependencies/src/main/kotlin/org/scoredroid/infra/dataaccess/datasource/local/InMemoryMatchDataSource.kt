package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest

interface InMemoryMatchDataSource {
    suspend fun saveMatch(match: Match): Match
    suspend fun renameMatch(matchId: Long, name: String): Result<Match>
    suspend fun getMatch(matchId: Long): Match?
    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match>
    suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match>
    suspend fun updateScoreTo(matchId: Long, teamAt: Int, newScore: Score): Result<Match>
    suspend fun getTeam(matchId: Long, teamAt: Int): Team?
    suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match>
    suspend fun getAllMatches(): List<Match>
    suspend fun clear()
}
