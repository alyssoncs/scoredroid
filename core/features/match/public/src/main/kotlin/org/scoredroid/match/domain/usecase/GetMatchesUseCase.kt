package org.scoredroid.match.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface GetMatchesUseCase {
    suspend operator fun invoke(): List<MatchResponse>
}
