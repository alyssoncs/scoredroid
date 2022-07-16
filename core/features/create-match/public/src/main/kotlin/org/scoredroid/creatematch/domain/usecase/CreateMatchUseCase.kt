package org.scoredroid.creatematch.domain.usecase

import org.scoredroid.data.response.MatchResponse


interface CreateMatchUseCase {
    suspend operator fun invoke(): MatchResponse
}