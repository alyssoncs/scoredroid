package org.scoredroid.usecase.test.doubles

import org.scoredroid.usecase.SaveMatchUseCase

class SaveMatchSpy : SaveMatchUseCase {

    private val invocations = hashSetOf<Long>()

    override suspend fun invoke(matchId: Long): Result<Unit> {
        invocations.add(matchId)
        return Result.success(Unit)
    }

    fun matchWithId(matchId: Long) = object : Assertions {
        override fun wasSaved(): Boolean {
            return invocations.contains(matchId)
        }
    }

    interface Assertions {
        fun wasSaved(): Boolean
    }
}