package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class IncrementScore(
    private val matchRepository: MatchRepository,
) : IncrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        val result = matchRepository.updateScore(matchId, teamAt) { it + increment }

        val e = result.exceptionOrNull()
        if (e != null) {
            return Result.failure(mapError(e))
        }

        return result
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