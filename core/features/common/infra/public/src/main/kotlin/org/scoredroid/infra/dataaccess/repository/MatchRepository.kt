package org.scoredroid.infra.dataaccess.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.orZero
import org.scoredroid.infra.dataaccess.datasource.local.MatchLocalDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {

    private val matchFlows = mutableMapOf<Long, MutableStateFlow<Match>>()

    suspend fun getMatchFlow(matchId: Long): Flow<Match>? {
        return getMatchMutableFlow(matchId)
    }

    suspend fun getMatch(matchId: Long): Match? {
        return matchLocalDataSource.getMatch(matchId)
    }

    suspend fun createMatch(createMatchRequest: CreateMatchRepositoryRequest): Match {
        return matchLocalDataSource.createMatch(createMatchRequest)
    }

    suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return matchLocalDataSource.addTeam(matchId, team)
    }

    suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return matchLocalDataSource.removeTeam(matchId, teamAt)
    }

    suspend fun updateScore(
        matchId: Long,
        teamAt: Int,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val currentScore = getCurrentScore(matchId, teamAt)
        return matchLocalDataSource.updateScoreTo(matchId, teamAt, update(currentScore))
            .updateFlowOnSuccess()
    }

    suspend fun updateScoreForAllTeams(
        matchId: Long,
        update: (currentScore: Score) -> Score,
    ): Result<Match> {
        val match = matchLocalDataSource.getMatch(matchId)
        return if (match != null) {
            updateScoreForAllTeams(match, update)
        } else {
            Result.failure(TeamOperationError.MatchNotFound)
        }
    }

    suspend fun renameMatch(matchId: Long, name: String): Result<Match> {
        val renameMatch = matchLocalDataSource.renameMatch(matchId, name)
        return renameMatch.updateFlowOnSuccess()
    }

    suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match> {
        return matchLocalDataSource.moveTeam(matchId, teamAt, moveTo)
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
        return matchLocalDataSource.getTeam(matchId, teamAt)?.score.orZero()
    }

    private suspend fun Result<Match>.updateFlowOnSuccess(): Result<Match> {
        return this.onSuccess { newMatchValue ->
            getMatchMutableFlow(newMatchValue.id)?.emit(newMatchValue)
        }
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
