package org.scoredroid.match.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.match.domain.request.CreateMatchRequestOptions

interface CreateMatchUseCase {
    suspend operator fun invoke(
        createMatchOptions: CreateMatchRequestOptions = CreateMatchRequestOptions()
    ): MatchResponse
}
