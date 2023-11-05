package org.scoredroid.usecase.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.RenameTeamUseCase

class RenameTeamSpy : RenameTeamUseCase {

    private val invocations = mutableMapOf<Pair<Long, Int>, String>()

    override suspend fun invoke(
        matchId: Long,
        teamAt: Int,
        newName: String,
    ): Result<MatchResponse> {
        invocations[matchId to teamAt] = newName
        return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
    }

    fun team(matchId: Long, teamAt: Int) = object : Assertions {
        override fun wasRenamedTo(name: String): Boolean {
            return invocations[matchId to teamAt] == name
        }
    }

    interface Assertions {
        fun wasRenamedTo(name: String): Boolean
    }
}
