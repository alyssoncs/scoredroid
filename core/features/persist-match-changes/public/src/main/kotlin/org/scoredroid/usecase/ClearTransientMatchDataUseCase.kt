package org.scoredroid.usecase

interface ClearTransientMatchDataUseCase {
    suspend operator fun invoke(matchId: Long): Result<Unit>
}
