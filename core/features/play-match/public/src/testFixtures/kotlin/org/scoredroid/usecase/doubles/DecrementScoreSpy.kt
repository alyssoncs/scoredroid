package org.scoredroid.usecase.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.DecrementScoreUseCase

class DecrementScoreSpy : DecrementScoreUseCase {
    var matchId: Long? = null
        private set
    var teamAt: Int? = null
        private set
    var decrement: Int? = null
        private set
    private var response: MatchResponse? = null

    fun setResponse(response: MatchResponse) {
        this.response = response
    }

    override suspend fun invoke(
        matchId: Long,
        teamAt: Int,
        decrement: Int,
    ): Result<MatchResponse> {
        this.matchId = matchId
        this.teamAt = teamAt
        this.decrement = decrement

        return if (response != null) {
            Result.success(response!!)
        } else {
            Result.failure(IllegalStateException("Spy not configured. Call setResponse"))
        }
    }
}
