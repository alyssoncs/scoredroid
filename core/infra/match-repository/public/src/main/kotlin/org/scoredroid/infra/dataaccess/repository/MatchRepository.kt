package org.scoredroid.infra.dataaccess.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.orZero
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    transientDataSource: TransientMatchDataSource,
    persistentDataSource: PersistentMatchDataSource,
) {

    companion object {
        private var instance: MatchRepository? = null

        fun getInstance(
            transientDataSource: TransientMatchDataSource,
            persistentDataSource: PersistentMatchDataSource,
        ): MatchRepository {
            val anInstance = instance
            if (anInstance != null)
                return anInstance
            instance = MatchRepository(transientDataSource, persistentDataSource)
            return getInstance(transientDataSource, persistentDataSource)
        }
    }

    private val dataSourceAggregator = DataSourceAggregator(
        transientDataSource,
        persistentDataSource,
    )

    private val matchesFlow = MutableStateFlow<List<Match>>(emptyList())

    suspend fun getMatchFlow(matchId: Long): Flow<Match?> {
        return getMatchesFlow().map { matches -> matches.find { match -> match.id == matchId } }
    }

    suspend fun getMatch(matchId: Long): Result<Match> {
        val match = dataSourceAggregator.getMatch(matchId)
        return if (match != null)
            Result.success(match)
        else
            Result.failure(Throwable("Match not found"))
    }

    suspend fun getMatchesFlow(): Flow<List<Match>> {
        val allMatches = dataSourceAggregator.getMatches()
        matchesFlow.value = allMatches

        return matchesFlow
    }

    suspend fun updateMatch(match: Match): Result<Match> {
        return updateAndEmitMatch(match.id) {
            Result.success(saveMatch(match))
        }
    }

    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
        return dataSourceAggregator.createMatch(createMatchRequest)
            .also { newMatch ->
                matchesFlow.update { currentMatches ->
                    currentMatches + newMatch
                }
            }
    }

    suspend fun updateScore(
        matchId: Long,
        teamAt: Int,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        return updateAndEmitMatch(matchId) {
            val currentScore = getCurrentScore(matchId, teamAt)
            updateScoreTo(matchId, teamAt, update(currentScore))
        }
    }

    suspend fun renameTeam(matchId: Long, teamAt: Int, newName: String): Result<Match> {
        return updateAndEmitMatch(matchId) {
            renameTeam(matchId, teamAt, newName)
        }
    }

    suspend fun persist(matchId: Long): Result<Unit> {
        return dataSourceAggregator.persist(matchId)
    }

    suspend fun removeMatch(matchId: Long): Result<Unit> {
        return dataSourceAggregator.removeMatch(matchId)
            .also { emitDeletedMatch(matchId) }
    }

    suspend fun clearTransientData(matchId: Long): Result<Unit> {
        return dataSourceAggregator.clearTransientData(matchId)
            .onSuccess { emitMatch(it) }
            .map { }
    }

    private suspend fun updateAndEmitMatch(
        matchId: Long,
        update: suspend TransientMatchDataSource.(matchId: Long) -> Result<Match>,
    ): Result<Match> {
        return dataSourceAggregator.updateMatch(matchId, update)
            .onSuccess { newMatch -> emitMatch(newMatch) }
    }

    private fun emitMatch(newMatch: Match) {
        matchesFlow.update { currentMatches ->
            currentMatches.map { match ->
                if (match.id == newMatch.id) newMatch else match
            }
        }
    }

    private fun emitDeletedMatch(matchId: Long) {
        matchesFlow.update { currentMatches ->
            currentMatches.filter { match -> match.id != matchId }
        }
    }

    private suspend fun getCurrentScore(matchId: Long, teamAt: Int): Score {
        return getMatch(matchId).getOrNull()?.teams?.getOrNull(teamAt)?.score.orZero()
    }

    private class DataSourceAggregator(
        private val transientDataSource: TransientMatchDataSource,
        private val persistentDataSource: PersistentMatchDataSource,
    ) {
        suspend fun getMatch(matchId: Long): Match? {
            return transientDataSource.getMatch(matchId)
                ?: updateTransient(matchId)
        }

        suspend fun getMatches(): List<Match> {
            val transientMatches = transientDataSource.getAllMatches()
            val persistedOnlyMatches = getPersistentOnlyMatches(transientMatches)

            return transientMatches + persistedOnlyMatches
        }

        private suspend fun getPersistentOnlyMatches(transientMatches: List<Match>): List<Match> {
            val persistentMatches = persistentDataSource.getAllMatches()

            return persistentMatches.notIn(transientMatches)
        }

        private fun List<Match>.notIn(
            other: List<Match>,
        ): List<Match> {
            val indexedOther = other.associateBy(Match::id)
            return filterNot { indexedOther.contains(it.id) }
        }

        suspend fun updateMatch(
            matchId: Long,
            update: suspend TransientMatchDataSource.(matchId: Long) -> Result<Match>,
        ): Result<Match> {
            val updateResult = transientDataSource.update(matchId)

            return if (updateResult.isFailure) {
                updateTransient(matchId)
                transientDataSource.update(matchId)
            } else {
                updateResult
            }
        }

        suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
            return transientDataSource.saveMatch(persistentDataSource.createMatch(createMatchRequest))
        }

        suspend fun removeMatch(matchId: Long): Result<Unit> {
            transientDataSource.removeMatch(matchId)
            return persistentDataSource.removeMatch(matchId)
        }

        suspend fun persist(matchId: Long): Result<Unit> {
            val match = getMatch(matchId)
            return if (match != null) {
                persistentDataSource.save(match)
            } else {
                Result.failure(Throwable("match not found"))
            }
        }

        suspend fun clearTransientData(matchId: Long): Result<Match> {
            transientDataSource.removeMatch(matchId)

            return runCatching { getMatch(matchId) ?: error("match with id $matchId not found") }
        }

        private suspend fun updateTransient(matchId: Long): Match? {
            return persistentDataSource.getMatch(matchId)?.also {
                transientDataSource.saveMatch(it)
            }
        }
    }
}
