package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface DecrementScoreUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int, decrement: Int = 1): Result<MatchResponse>
}