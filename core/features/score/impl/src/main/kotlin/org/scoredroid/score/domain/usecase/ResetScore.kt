package org.scoredroid.score.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Score
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class ResetScore(
    private val matchRepository: MatchRepository,
) : ResetScoreUseCase {
    override suspend fun invoke(matchId: Long): Result<MatchResponse> {
        val result = matchRepository.resetScore(matchId)

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