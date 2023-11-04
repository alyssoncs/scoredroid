package org.scoredroid.usecase

interface RemoveMatchUseCase {
    suspend operator fun invoke(matchId: Long): Result<Unit>
}
