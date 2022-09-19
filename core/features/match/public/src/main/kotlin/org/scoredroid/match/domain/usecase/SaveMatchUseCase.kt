package org.scoredroid.match.domain.usecase

interface SaveMatchUseCase {
    suspend operator fun invoke(matchId: Long): Result<Unit>
}
