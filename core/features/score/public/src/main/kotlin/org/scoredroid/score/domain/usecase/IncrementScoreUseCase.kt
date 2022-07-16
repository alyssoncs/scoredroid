package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface IncrementScoreUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse>
    suspend operator fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse>

    sealed class Error : Throwable() {
        object MatchNotFound : Error()
        object TeamNotFound : Error()
    }
}