package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse

class DecrementScore(
    private val scoreUpdater: ScoreUpdater,
) : DecrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, decrement: Int): Result<MatchResponse> {
        return scoreUpdater.updateForTeam(matchId, teamAt) { currentScore -> currentScore - decrement }
    }
}
