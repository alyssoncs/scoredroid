package org.scoredroid.match.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.scoredroid.data.response.MatchResponse

interface GetMatchFlowUseCase {
    suspend operator fun invoke(matchId: Long): Flow<MatchResponse?>
}
