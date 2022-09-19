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
    private val matches = mutableMapOf<Long, Match>()

    override suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match {
        return Match(
            id = nextId,
            name = matchRequest.name,
            teams = matchRequest.teams.map { Team(name = it.name, score = 0.toScore()) },
        ).also { updateNextId(); saveOnCache(it) }
    }

    override suspend fun getMatch(matchId: Long): Match? {
        return matches[matchId]
    }

    override suspend fun save(match: Match) {
        saveOnCache(match)
    }

    override suspend fun removeMatch(matchId: Long): Result<Unit> {
        return matches.remove(matchId).let {
            if (it == null) {
                Result.failure(Throwable())
            } else {
                Result.success(Unit)
            }
        }
    }

    private fun updateNextId() {
        nextId = matchIdStrategy.invoke(nextId)
    }

    private fun saveOnCache(match: Match) {
        matches[match.id] = match
    }
}
