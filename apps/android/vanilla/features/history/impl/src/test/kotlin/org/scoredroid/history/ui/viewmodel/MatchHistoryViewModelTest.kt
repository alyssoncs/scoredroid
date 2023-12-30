package org.scoredroid.history.ui.viewmodel

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.usecase.doubles.RemoveMatchUseCaseSpy
import org.scoredroid.usecase.test.doubles.GetMatchesFlowStub
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

    private val getMatchesFlowUseCaseStub = GetMatchesFlowStub(matchResponse)
    private val removeMatchUseCaseSpy = RemoveMatchUseCaseSpy()

    private val matchHistoryViewModel =
        MatchHistoryViewModel(getMatchesFlowUseCaseStub, removeMatchUseCaseSpy)

    @Test
    fun `should fetch matches from use case`() = runTest {
        matchHistoryViewModel.uiState.test {
            val loading = awaitItem()
            loading.shouldBeInstanceOf<MatchHistoryUiModel.Loading>()

            val content = awaitItem() as MatchHistoryUiModel.Content
            val actualMatch = content.matches.first()
            val expectedMatch = expectedUiModel.matches.first()
            actualMatch.matchName shouldBe expectedMatch.matchName
            actualMatch.id shouldBe expectedMatch.id
            actualMatch.numberOfTeams shouldBe expectedMatch.numberOfTeams
        }
    }

    @Test
    fun `flow update updates the ui state`() = runTest {
        matchHistoryViewModel.uiState.test {
            awaitItem().shouldBeInstanceOf<MatchHistoryUiModel.Loading>()
            awaitItem().shouldBeInstanceOf<MatchHistoryUiModel.Content>()

            getMatchesFlowUseCaseStub.emitNewMatch(
                MatchResponse(
                    id = 5L,
                    name = "match name",
                    teams = listOf(TeamResponse(name = "team name", score = 0)),
                ),
            )

            val content = awaitItem() as MatchHistoryUiModel.Content
            content.matches shouldHaveSize 2
        }
    }

    @Test
    fun `remove match calls use case`() = runTest {
        matchHistoryViewModel.uiState.test {
            skipItems(1)
            (awaitItem() as MatchHistoryUiModel.Content).matches[0].onRemove()
            cancelAndIgnoreRemainingEvents()
        }

        removeMatchUseCaseSpy.removedMatchId shouldBe 5L
    }
}
