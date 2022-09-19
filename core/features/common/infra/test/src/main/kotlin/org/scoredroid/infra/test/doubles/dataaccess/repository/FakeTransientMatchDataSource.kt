package org.scoredroid.infra.test.doubles.dataaccess.repository

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest

class FakeTransientMatchDataSource : TransientMatchDataSource {
    private val matches = mutableMapOf<Long, Match>()

    override suspend fun saveMatch(match: Match): Match {
        return saveOnCache(match)
    }

    override suspend fun addTeam(matchId: Long, team: AddTeamRepositoryRequest): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(
                teams = match.teams + Team(name = team.name, score = 0.toScore())
            )
        }
    }

    override suspend fun removeTeam(matchId: Long, teamAt: Int): Result<Match> {
        return updateMatch(matchId) { match ->
            match.copy(
                teams = match.teams.filterIndexed { idx, _ -> idx != teamAt }
            )
        }
    }

    override suspend fun getMatch(matchId: Long): Match? {
        return matches[matchId]
    }

    override suspend fun updateScoreTo(
        matchId: Long,
        teamAt: Int,
        newScore: Score
    ): Result<Match> {
        return updateMatch(matchId, onUpdateError = TeamOperationError.TeamNotFound) { match ->
            match.updateScore(teamAt, newScore).takeIf { teamAt in match.teams.indices }
        }
    }

    override suspend fun moveTeam(matchId: Long, teamAt: Int, moveTo: Int): Result<Match> {
        return updateMatch(matchId, onUpdateError = TeamOperationError.TeamNotFound) { match ->
            if (teamAt in match.teams.indices) {
                match.copy(teams = match.moveTeam(teamAt, moveTo))
            } else {
                null
            }
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
                Result.failure(Throwable())
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

    private fun Match.updateScore(
        teamAt: Int,
        newScore: Score
    ): Match {
        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt) {
                    team.copy(score = newScore)
                } else {
                    team
                }
            }
        )
    }

    private fun Match.moveTeam(
        teamAt: Int,
        moveTo: Int
    ): List<Team> {
        val teams = teams.toMutableList()
        val indexToMove = moveTo.coerceIn(teams.indices)
        val removed = teams.removeAt(teamAt)
        teams.add(indexToMove, removed)
        return teams.toList()
    }

    private suspend fun updateMatch(
        matchId: Long,
        onUpdateError: Throwable = Throwable(),
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
