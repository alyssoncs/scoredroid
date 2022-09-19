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
    private val transientDataSource: TransientMatchDataSource,
    private val persistentDataSource: PersistentMatchDataSource
) {

    private val matchFlows = mutableMapOf<Long, MutableStateFlow<Match>>()

    suspend fun getMatchFlow(matchId: Long): Flow<Match>? {
        return getMatchMutableFlow(matchId)
    }

    suspend fun getMatch(matchId: Long): Match? {
        return transientDataSource.getMatch(matchId)
            ?: cachePersistenceIntoTransient(matchId)
    }

    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
        return transientDataSource.saveMatch(persistentDataSource.createMatch(createMatchRequest))
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

    suspend fun persist(matchId: Long): Result<Unit> {
        val match = getMatch(matchId)
        return if (match != null) {
            persistentDataSource.save(match)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun removeMatch(matchId: Long): Result<Unit> {
        transientDataSource.removeMatch(matchId)
        return persistentDataSource.removeMatch(matchId)
    }

    private suspend fun updateMatch(
        matchId: Long,
        update: suspend TransientMatchDataSource.(matchId: Long) -> Result<Match>,
    ): Result<Match> {
        val updateResult = transientDataSource.update(matchId)

        return if (updateResult.exceptionOrNull() is TeamOperationError.MatchNotFound) {
            cachePersistenceIntoTransient(matchId)
            transientDataSource.update(matchId)
        } else {
            updateResult
        }.onSuccess { newMatch ->
            getMatchMutableFlow(newMatch.id)?.emit(newMatch)
        }
    }

    private suspend fun cachePersistenceIntoTransient(matchId: Long): Match? {
        return persistentDataSource.getMatch(matchId)?.also { transientDataSource.saveMatch(it) }
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
}
