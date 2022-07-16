package org.scoredroid.creatematch.domain.usecase

import org.scoredroid.creatematch.domain.response.MatchResponse

interface CreateMatchUseCase {
    suspend operator fun invoke(): MatchResponse
}