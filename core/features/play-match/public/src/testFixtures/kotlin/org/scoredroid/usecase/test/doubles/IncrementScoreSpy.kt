package org.scoredroid.usecase.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.IncrementScoreUseCase

class IncrementScoreSpy : IncrementScoreUseCase {
    var matchId: Long? = null
        private set
    var teamAt: Int? = null
        private set
    var increment: Int? = null
        private set
    private var response: MatchResponse? = null

    fun setResponse(response: MatchResponse) {
        this.response = response
    }

    override suspend fun invoke(
        matchId: Long,
        teamAt: Int,
        increment: Int,
    ): Result<MatchResponse> {
        this.matchId = matchId
        this.teamAt = teamAt
        this.increment = increment

        return if (response != null) {
            Result.success(response!!)
        } else {
            Result.failure(IllegalStateException("Spy not configured. Call setResponse"))
        }
    }
}
