package org.scoredroid.usecase

import kotlinx.coroutines.flow.Flow
import org.scoredroid.data.response.MatchResponse

interface GetMatchesUseCase {
    suspend operator fun invoke(): Flow<List<MatchResponse>>
}
