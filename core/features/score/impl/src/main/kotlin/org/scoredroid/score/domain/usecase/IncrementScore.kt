package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class IncrementScore(
    private val scoreUpdater: ScoreUpdater,
) : IncrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        return scoreUpdater.update(matchId, teamAt) { currentScore -> currentScore + increment }
    }
}