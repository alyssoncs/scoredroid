package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

class IncrementScore(
    private val scoreUpdater: ScoreUpdater,
) : IncrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        return scoreUpdater.updateForTeam(matchId, teamAt) { currentScore -> currentScore + increment }
    }
}