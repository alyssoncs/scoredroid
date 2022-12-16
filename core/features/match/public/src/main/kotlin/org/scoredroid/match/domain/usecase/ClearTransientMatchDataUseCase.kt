package org.scoredroid.match.domain.usecase

interface ClearTransientMatchDataUseCase {
    suspend operator fun invoke()
}
