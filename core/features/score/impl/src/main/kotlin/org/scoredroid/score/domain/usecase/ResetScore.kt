package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

class ResetScore(
    private val scoreUpdater: ScoreUpdater,
) : ResetScoreUseCase {
    override suspend fun invoke(matchId: Long): Result<MatchResponse> {
        //TODO: update flow only once (bump turbine to see test fail)
        return scoreUpdater.updateForAllTeams(matchId) {currentScore -> currentScore * 0}
    }
}
