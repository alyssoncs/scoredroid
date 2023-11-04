package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class IncrementScore(
    matchRepository: MatchRepository,
) : IncrementScoreUseCase {
    private val scoreUpdater = ScoreUpdater(matchRepository)

    override suspend fun invoke(matchId: Long, teamAt: Int, increment: Int): Result<MatchResponse> {
        return scoreUpdater.updateForTeam(matchId, teamAt) { currentScore -> currentScore + increment }
    }
}
