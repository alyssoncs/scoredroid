package org.scoredroid.usecase

interface SaveMatchUseCase {
    suspend operator fun invoke(matchId: Long): Result<Unit>
}
