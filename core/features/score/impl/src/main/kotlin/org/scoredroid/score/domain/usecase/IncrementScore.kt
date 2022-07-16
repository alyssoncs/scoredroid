package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class IncrementScore(
    private val matchRepository: MatchRepository
) : IncrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse> {
        return invoke(matchId, teamAt, 1)
    }

    override suspend fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        val result = matchRepository.incrementScoreBy(matchId, teamAt, increment)

        val e = result.exceptionOrNull()
        if (e != null) {
            return Result.failure(mapError(e))
        }

        return result
    }

    private fun mapError(e: Throwable): IncrementScoreUseCase.Error {
        val newException = when (e) {
            TeamOperationError.MatchNotFound -> IncrementScoreUseCase.Error.MatchNotFound
            TeamOperationError.TeamNotFound -> IncrementScoreUseCase.Error.TeamNotFound
            else -> IncrementScoreUseCase.Error.TeamNotFound
        }
        return newException
    }
}