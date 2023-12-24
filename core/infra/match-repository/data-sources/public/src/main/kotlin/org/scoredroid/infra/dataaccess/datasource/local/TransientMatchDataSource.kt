package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score

interface TransientMatchDataSource {
    suspend fun saveMatch(match: Match): Match
    suspend fun getMatch(matchId: Long): Result<Match>
    suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match>
    suspend fun renameTeam(matchId: Long, teamAt: Int, newName: String): Result<Match>
    suspend fun updateScoreTo(matchId: Long, teamAt: Int, newScore: Score): Result<Match>
    suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match>
    suspend fun getAllMatches(): List<Match>
    suspend fun clear()
    suspend fun removeMatch(matchId: Long): Result<Unit>
}
