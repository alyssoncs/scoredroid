package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

interface PersistentMatchDataSource {
    suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match
}