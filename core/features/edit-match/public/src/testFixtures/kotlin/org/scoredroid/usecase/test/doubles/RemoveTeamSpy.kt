package org.scoredroid.usecase.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.RemoveTeamUseCase

class RemoveTeamSpy : RemoveTeamUseCase {

    private val invocations = mutableMapOf<Long, Int>()

    override suspend fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse> {
        invocations[matchId] = teamAt
        return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
    }

    fun matchWithId(matchId: Long) = object : Assertions {
        override fun hadTeamRemoved(teamAt: Int): Boolean {
            return invocations[matchId] == teamAt
        }
    }

    interface Assertions {
        fun hadTeamRemoved(teamAt: Int): Boolean
    }
}
