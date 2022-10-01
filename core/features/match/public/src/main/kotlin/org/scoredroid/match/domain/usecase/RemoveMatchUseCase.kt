package org.scoredroid.match.domain.usecase

interface RemoveMatchUseCase {
    suspend operator fun invoke(matchId: Long): Result<Unit>
}
