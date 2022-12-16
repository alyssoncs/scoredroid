package org.scoredroid.infra.dataaccess.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.orZero
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    transientDataSource: TransientMatchDataSource,
    persistentDataSource: PersistentMatchDataSource
) {

    companion object {
        private var instance: MatchRepository? = null

        fun getInstance(
            transientDataSource: TransientMatchDataSource,
            persistentDataSource: PersistentMatchDataSource
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

    private val matchFlows = mutableMapOf<Long, MutableStateFlow<Match>>()

    suspend fun getMatchFlow(matchId: Long): Flow<Match>? {
        return getMatchMutableFlow(matchId)
    }

    suspend fun getMatch(matchId: Long): Match? {
        return dataSourceAggregator.getMatch(matchId)
    }

    suspend fun getMatches(): List<Match> {
        return dataSourceAggregator.getMatches()
    }

    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
        return dataSourceAggregator.createMatch(createMatchRequest)
    }

    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return updateMatch(matchId) { addTeam(matchId, team) }
    }

    suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return updateMatch(matchId) { removeTeam(matchId, teamAt) }
    }

    suspend fun updateScore(
        matchId: Long,
        teamAt: Int,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val currentScore = getCurrentScore(matchId, teamAt)
        return updateMatch(matchId) { updateScoreTo(matchId, teamAt, update(currentScore)) }
    }

    suspend fun updateScoreForAllTeams(
        matchId: Long,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val match = getMatch(matchId)
        return if (match != null) {
            updateScoreForAllTeams(match, update)
        } else {
            Result.failure(TeamOperationError.MatchNotFound)
        }
    }

    suspend fun renameMatch(matchId: Long, name: String): Result<Match> {
        return updateMatch(matchId) { renameMatch(matchId, name) }
    }

    suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match> {
        return updateMatch(matchId) { moveTeam(matchId, teamAt, moveTo) }
    }

    suspend fun renameTeam(matchId: Long, teamAt: Int, newName: String): Result<Match> {
        return updateMatch(matchId) {
            renameTeam(matchId, teamAt, newName)
        }
    }

    suspend fun persist(matchId: Long): Result<Unit> {
        return dataSourceAggregator.persist(matchId)
    }

    suspend fun removeMatch(matchId: Long): Result<Unit> {
        return dataSourceAggregator.removeMatch(matchId)
    }

    suspend fun clearTransientData() {
        return dataSourceAggregator.clearTransientData()
    }

    private suspend fun updateMatch(
        matchId: Long,
        update: suspend TransientMatchDataSource.(matchId: Long) -> Result<Match>,
    ): Result<Match> {
        return dataSourceAggregator.updateMatch(matchId, update)
            .onSuccess { newMatch -> emitMatch(newMatch) }
    }

    private suspend fun emitMatch(newMatch: Match) {
        getMatchMutableFlow(newMatch.id)?.emit(newMatch)
    }

    private suspend fun updateScoreForAllTeams(
        match: Match,
        update: (currentScore: Score) -> Score
    ): Result<Match> {
        var result: Result<Match> = Result.success(match)
        match.teams.forEachIndexed { teamAt, _ ->
            result = updateScore(match.id, teamAt, update)
        }
        return result
    }

    private suspend fun getCurrentScore(matchId: Long, teamAt: Int): Score {
        return getMatch(matchId)?.teams?.getOrNull(teamAt)?.score.orZero()
    }

    private suspend fun getMatchMutableFlow(matchId: Long): MutableStateFlow<Match>? {
        val flow = matchFlows[matchId]

        return if (flow != null) {
            flow
        } else {
            val match = getMatch(matchId)
            if (match != null) {
                matchFlows[matchId] = MutableStateFlow(match)
                getMatchMutableFlow(matchId)
            } else {
                null
            }
        }
    }

    private class DataSourceAggregator(
        private val transientDataSource: TransientMatchDataSource,
        private val persistentDataSource: PersistentMatchDataSource
    ) {
        suspend fun getMatch(matchId: Long): Match? {
            return transientDataSource.getMatch(matchId)
                ?: updateTransient(matchId)
        }

        suspend fun getMatches(): List<Match> {
            val persistedMatches = persistentDataSource.getAllMatches()
            val transientOnlyMatches = getTransientOnlyMatches(persistedMatches)

            return persistedMatches + transientOnlyMatches
        }

        private suspend fun getTransientOnlyMatches(persistedMatches: List<Match>): List<Match> {
            val transientMatches = transientDataSource.getAllMatches()

            if (transientMatches.isEmpty())
                return transientMatches

            return transientMatches.notIn(persistedMatches)
        }

        private fun List<Match>.notIn(
            persistedMatches: List<Match>
        ): List<Match> {
            val indexedPersistedMatches = persistedMatches.associateBy(Match::id)
            return filterNot { indexedPersistedMatches.contains(it.id) }
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

        suspend fun clearTransientData() {
            return transientDataSource.clear()
        }

        private suspend fun updateTransient(matchId: Long): Match? {
            return persistentDataSource.getMatch(matchId)?.also {
                transientDataSource.saveMatch(it)
            }
        }
    }
}
