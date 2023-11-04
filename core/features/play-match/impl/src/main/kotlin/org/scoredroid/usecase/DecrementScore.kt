package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class DecrementScore(
    matchRepository: MatchRepository,
) : DecrementScoreUseCase {
    private val scoreUpdater = ScoreUpdater(matchRepository)

    override suspend fun invoke(matchId: Long, teamAt: Int, decrement: Int): Result<MatchResponse> {
        return scoreUpdater.updateForTeam(matchId, teamAt) { currentScore -> currentScore - decrement }
    }
}
