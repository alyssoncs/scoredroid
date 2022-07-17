package org.scoredroid.score.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class ScoreUpdater(
    private val matchRepository: MatchRepository,
) {
    suspend fun update(
        matchId: Long,
        teamAt: Int,
        updateStrategy: (currentScore: Int) -> Int,
    ): Result<MatchResponse> {
        val result = matchRepository.updateScore(matchId, teamAt, updateStrategy)

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
