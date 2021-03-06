package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

class ResetScore(
    private val scoreUpdater: ScoreUpdater,
) : ResetScoreUseCase {
    override suspend fun invoke(matchId: Long): Result<MatchResponse> {
        return scoreUpdater.updateForAllTeams(matchId) {currentScore -> currentScore * 0}
    }
}