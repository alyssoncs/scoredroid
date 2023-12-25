package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match

class InMemoryMatchDataSource private constructor() : TransientMatchDataSource {
    companion object {
        val instance by lazy { newInstance() }
        fun newInstance(): TransientMatchDataSource = InMemoryMatchDataSource()
    }

    private val matches = mutableMapOf<Long, Match>()

    override suspend fun saveMatch(match: Match): Match {
        return saveOnCache(match)
    }

    override suspend fun getMatch(matchId: Long): Match? {
        return matches[matchId]
    }

    override suspend fun getAllMatches(): List<Match> {
        return matches.values.toList()
    }

    override suspend fun clear() {
        matches.clear()
    }

    override suspend fun removeMatch(matchId: Long): Result<Unit> {
        return matches.remove(matchId).let {
            if (it == null) {
                Result.failure(Throwable("match not found"))
            } else {
                Result.success(Unit)
            }
        }
    }

    private fun saveOnCache(match: Match): Match {
        matches[match.id] = match
        return match
    }
}
