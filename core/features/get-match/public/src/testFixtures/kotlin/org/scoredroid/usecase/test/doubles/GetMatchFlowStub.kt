package org.scoredroid.usecase.test.doubles

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.GetMatchFlowUseCase
import kotlin.time.Duration.Companion.seconds

class GetMatchFlowStub : GetMatchFlowUseCase {
    var response: MatchResponse? = null

    override suspend fun invoke(matchId: Long): Flow<MatchResponse?> {
        delay(1.seconds)
        return flowOf(response)
    }
}
