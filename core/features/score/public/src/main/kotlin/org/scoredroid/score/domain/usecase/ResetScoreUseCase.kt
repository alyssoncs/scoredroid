package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface ResetScoreUseCase {
    suspend operator fun invoke(matchId: Long): Result<MatchResponse>
}