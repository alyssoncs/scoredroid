package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class ScoreUpdater(
    private val matchRepository: MatchRepository,
) {
    suspend fun updateForTeam(
        matchId: Long,
        teamAt: Int,
        updateScore: (currentScore: Score) -> Score,
    ): Result<MatchResponse> {
        return updateScore { updateScore(matchId, teamAt, updateScore) }
    }

    suspend fun updateForAllTeams(
        matchId: Long,
        updateScore: (currentScore: Score) -> Score,
    ): Result<MatchResponse> {
        return updateScore { updateScoreForAllTeams(matchId, updateScore) }
    }

    private suspend fun updateScore(
        updateStrategy: suspend MatchRepository.() -> Result<Match>,
    ): Result<MatchResponse> {
        val result = matchRepository.updateStrategy()

        val e = result.exceptionOrNull()
        if (e != null) {
            return Result.failure(mapError(e))
        }

        return result.map { it.toMatchResponse() }
    }

    private fun mapError(e: Throwable): UpdateScoreError {
        val newException = when (e) {
            TeamOperationError.MatchNotFound -> UpdateScoreError.MatchNotFound
            TeamOperationError.TeamNotFound -> UpdateScoreError.TeamNotFound
            else -> UpdateScoreError.TeamNotFound
        }
        return newException
    }
}
