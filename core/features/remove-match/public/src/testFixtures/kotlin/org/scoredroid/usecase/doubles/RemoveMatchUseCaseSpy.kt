package org.scoredroid.usecase.doubles

import org.scoredroid.usecase.RemoveMatchUseCase

class RemoveMatchUseCaseSpy : RemoveMatchUseCase {
    var removedMatchId: Long? = null

    override suspend fun invoke(matchId: Long): Result<Unit> {
        removedMatchId = matchId
        return Result.success(Unit)
    }
}
