package org.scoredroid.usecase.test.doubles

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.GetMatchesFlowUseCase
import kotlin.time.Duration.Companion.seconds

class GetMatchesFlowStub(initialValue: List<MatchResponse>) : GetMatchesFlowUseCase {
    private val responseFlow: MutableStateFlow<List<MatchResponse>> = MutableStateFlow(initialValue)

    override suspend fun invoke(): Flow<List<MatchResponse>> {
        delay(1.seconds)
        return responseFlow
    }

    fun emitNewMatch(response: MatchResponse) {
        responseFlow.update {
            it + response
        }
    }
}