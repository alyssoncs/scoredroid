package org.scoredroid.infra.test.doubles.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class FakePersistentMatchDataSource(
    initialMatchId: Long = 0L,
    private val matchIdStrategy: (currentId: Long) -> Long = { currentId -> currentId.inc() },
) : PersistentMatchDataSource {
    private var nextId = initialMatchId
    private val matches = mutableMapOf<Long, Match>()

    private var exceptionToFail: Throwable? = null

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

    override suspend fun getAllMatches(): List<Match> {
        return matches.values.toList()
    }

    override suspend fun save(match: Match): Result<Unit> {
        val exception = exceptionToFail

        return if (exception != null) {
            Result.failure(exception)
        } else {
            saveOnCache(match)
            Result.success(Unit)
        }
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

    fun failWith(exception: Throwable) {
        exceptionToFail = exception
    }
}
