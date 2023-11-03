package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse

interface CreateMatchUseCase {
    suspend operator fun invoke(
        createMatchOptions: CreateMatchRequestOptions = CreateMatchRequestOptions(),
    ): MatchResponse
}
