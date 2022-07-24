package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface IncrementScoreUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int, increment: Int = 1): Result<MatchResponse>
}
