package org.scoredroid.history.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.RemoveMatchUseCase
import org.scoredroid.viewmodel.CoroutineTestExtension

@ExtendWith(CoroutineTestExtension::class)
class MatchHistoryViewModelTest {

    private val matchResponse = listOf(
        MatchResponse(
            id = 5L,
            name = "match name",
            teams = listOf(TeamResponse(name = "team name", score = 0)),
        ),
    )

    private val expectedUiModel = MatchHistoryUiModel.Content(
        matches = listOf(
            MatchHistoryUiModel.Content.Match(
                matchName = "match name",
                numberOfTeams = 1,
                id = 5L,
            ),
        ),
    )

    private val getMatchesFlowUseCaseStub = GetMatchesFlowUseCaseStub(matchResponse)
    private val removeMatchUseCaseSpy = RemoveMatchUseCaseSpy()

    private val matchHistoryViewModel = MatchHistoryViewModel(getMatchesFlowUseCaseStub, removeMatchUseCaseSpy)

    @Test
    fun `should fetch matches from use case`() = runTest {
        matchHistoryViewModel.uiModel.test {
            val loading = awaitItem()
            assertThat(loading).isInstanceOf(MatchHistoryUiModel.Loading::class.java)

            val content = awaitItem()
            val value = content as MatchHistoryUiModel.Content
            assertThat(value.matches).containsExactlyElementsIn(expectedUiModel.matches)
        }
    }

    @Test
    fun `flow update updates the ui state`() = runTest {
        matchHistoryViewModel.uiModel.test {
            assertThat(awaitItem()).isInstanceOf(MatchHistoryUiModel.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(MatchHistoryUiModel.Content::class.java)

            getMatchesFlowUseCaseStub.emitNewMatch(
                MatchResponse(
                    id = 5L,
                    name = "match name",
                    teams = listOf(TeamResponse(name = "team name", score = 0)),
                ),
            )

            val content = awaitItem() as MatchHistoryUiModel.Content
            assertThat(content.matches).hasSize(2)
        }
    }

    @Test
    fun `remove match calls use case`() = runTest {
        matchHistoryViewModel.removeMatch(0L)

        assertThat(removeMatchUseCaseSpy.removedMatchId).isEqualTo(0L)
    }

    class GetMatchesFlowUseCaseStub(initialValue: List<MatchResponse>) : GetMatchesFlowUseCase {
        private val responseFlow: MutableStateFlow<List<MatchResponse>> = MutableStateFlow(initialValue)

        override suspend fun invoke(): Flow<List<MatchResponse>> {
            delay(1000)
            return responseFlow
        }

        fun emitNewMatch(response: MatchResponse) {
            responseFlow.update {
                it + response
            }
        }
    }

    class RemoveMatchUseCaseSpy : RemoveMatchUseCase {
        var removedMatchId: Long? = null

        override suspend fun invoke(matchId: Long): Result<Unit> {
            removedMatchId = matchId
            return Result.success(Unit)
        }
    }
}
