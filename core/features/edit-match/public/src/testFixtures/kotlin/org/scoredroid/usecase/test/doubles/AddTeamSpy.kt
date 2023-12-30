package org.scoredroid.usecase.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.AddTeamRequest
import org.scoredroid.usecase.AddTeamUseCase

class AddTeamSpy : AddTeamUseCase {

    private val invocations = mutableMapOf<Long, AddTeamRequest>()

    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        invocations[matchId] = team
        return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
    }

    fun matchWithId(matchId: Long) = object : Assertions {
        override fun hasNewTeam(team: AddTeamRequest): Boolean {
            return invocations[matchId] == team
        }
    }

    interface Assertions {
        fun hasNewTeam(team: AddTeamRequest): Boolean
    }
}