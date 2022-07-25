package org.scoredroid.infra.test.fixtures.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepositoryFixture(val repository: MatchRepository) {
    suspend fun createEmptyMatch(): Match {
        return repository.createMatch(CreateMatchRepositoryRequest())
    }

    suspend fun createNamedMatch(matchName: String): Match {
        return repository.createMatch(CreateMatchRepositoryRequest(name = matchName))
    }
}