package org.scoredroid.usecase.doubles

import org.scoredroid.usecase.ClearTransientMatchDataUseCase

class ClearTransientMatchDataSpy : ClearTransientMatchDataUseCase {

    private val invocations = hashSetOf<Long>()

    override suspend fun invoke(matchId: Long): Result<Unit> {
        invocations.add(matchId)
        return Result.success(Unit)
    }

    fun matchWithId(matchId: Long) = object : Assertions {
        override fun wasCleared(): Boolean {
            return invocations.contains(matchId)
        }
    }

    interface Assertions {
        fun wasCleared(): Boolean
    }
}
