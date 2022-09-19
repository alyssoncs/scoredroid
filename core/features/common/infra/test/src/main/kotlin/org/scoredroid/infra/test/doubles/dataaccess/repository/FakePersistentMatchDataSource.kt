package org.scoredroid.infra.test.doubles.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class FakePersistentMatchDataSource(
    initialMatchId: Long,
    private val matchIdStrategy: (currentId: Long) -> Long,
) : PersistentMatchDataSource {
    private var nextId = initialMatchId

    override suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match {
        return Match(
            id = nextId,
            name = matchRequest.name,
            teams = matchRequest.teams.map { Team(name = it.name, score = 0.toScore()) },
        ).also { updateNextId() }
    }

    private fun updateNextId() {
        nextId = matchIdStrategy.invoke(nextId)
    }
}
