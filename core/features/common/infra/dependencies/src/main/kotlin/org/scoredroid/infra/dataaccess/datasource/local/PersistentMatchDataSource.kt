package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

interface PersistentMatchDataSource {
    suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match
    suspend fun getMatch(matchId: Long): Match?
    suspend fun save(match: Match)
    suspend fun removeMatch(matchId: Long): Result<Unit>
}