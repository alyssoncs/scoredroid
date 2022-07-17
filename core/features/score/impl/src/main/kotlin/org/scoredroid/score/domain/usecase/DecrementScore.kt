package org.scoredroid.score.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class DecrementScore(
    private val scoreUpdater: ScoreUpdater,
) : DecrementScoreUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, decrement: Int): Result<MatchResponse> {
        return scoreUpdater.update(matchId, teamAt) { currentScore -> currentScore - decrement }
    }
}