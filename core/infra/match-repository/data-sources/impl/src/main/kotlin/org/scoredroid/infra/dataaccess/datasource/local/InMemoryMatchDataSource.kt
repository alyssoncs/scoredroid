package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest

class InMemoryMatchDataSource private constructor() : TransientMatchDataSource {
    companion object {
        val instance by lazy { newInstance() }
        fun newInstance(): TransientMatchDataSource = InMemoryMatchDataSource()
    }

    private val matches = mutableMapOf<Long, Match>()

    override suspend fun saveMatch(match: Match): Match {
        return saveOnCache(match)
    }

    override suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return updateMatch(matchId) { match ->
            match.addTeam(Team(name = team.name, score = 0.toScore()))
        }
    }

    override suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return updateMatch(matchId) { match ->
            runCatching { match.removeTeam(teamAt) }
                .recover { match }
                .getOrNull()
        }
    }

    override suspend fun renameTeam(matchId: Long, teamAt: Int, newName: String): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(
                teams = match.teams.mapIndexed { idx, team -> if (idx == teamAt) team.copy(name = newName) else team },
            )
        }
    }

    override suspend fun getMatch(matchId: Long): Match? {
        return matches[matchId]
    }

    override suspend fun updateScoreTo(
        matchId: Long,
        teamAt: Int,
        newScore: Score,
    ): Result<Match> {
        return updateMatch(matchId, onUpdateError = TeamOperationError.TeamNotFound) { match ->
            match.runCatching { match.updateScore(teamAt, newScore) }
                .getOrNull()
        }
    }

    override suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match> {
        return updateMatch(matchId, onUpdateError = TeamOperationError.TeamNotFound) { match ->
            runCatching { match.moveTeam(teamAt, moveTo) }
                .getOrNull()
        }
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

    override suspend fun renameMatch(matchId: Long, name: String): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(name = name)
        }
    }

    private suspend fun updateMatch(
        matchId: Long,
        onUpdateError: Throwable = Throwable("an error occurred while updating the match"),
        update: (Match) -> Match?,
    ): Result<Match> {
        val match = getMatch(matchId) ?: return Result.failure(TeamOperationError.MatchNotFound)

        val updatedMatch = update(match)
        return if (updatedMatch != null) {
            Result.success(saveOnCache(updatedMatch))
        } else {
            Result.failure(onUpdateError)
        }
    }

    private fun saveOnCache(match: Match): Match {
        matches[match.id] = match
        return match
    }
}
