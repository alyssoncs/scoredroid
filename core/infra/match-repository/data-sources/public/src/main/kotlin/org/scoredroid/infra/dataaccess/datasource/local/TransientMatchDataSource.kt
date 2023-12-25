package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match

interface TransientMatchDataSource {
    suspend fun saveMatch(match: Match): Match
    suspend fun getMatch(matchId: Long): Match?
    suspend fun getAllMatches(): List<Match>
    suspend fun clear()
    suspend fun removeMatch(matchId: Long): Result<Unit>
}
