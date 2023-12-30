package org.scoredroid.usecase.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.RenameMatchUseCase

class RenameMatchSpy : RenameMatchUseCase {

    private val invocations = mutableMapOf<Long, String>()

    override suspend fun invoke(matchId: Long, name: String): Result<MatchResponse> {
        invocations[matchId] = name
        return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
    }

    fun matchWithId(matchId: Long) = object : Assertions {
        override fun wasRenamedTo(name: String): Boolean {
            return invocations[matchId] == name
        }
    }

    interface Assertions {
        fun wasRenamedTo(name: String): Boolean
    }
}