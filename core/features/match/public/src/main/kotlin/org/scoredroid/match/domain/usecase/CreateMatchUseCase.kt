package org.scoredroid.match.domain.usecase

import org.scoredroid.data.response.MatchResponse


interface CreateMatchUseCase {
    suspend operator fun invoke(): MatchResponse
}