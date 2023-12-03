package org.scoredroid.history.ui.viewmodel

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.usecase.doubles.GetMatchesFlowStub
import org.scoredroid.usecase.doubles.RemoveMatchUseCaseSpy
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

    private val matchHistoryViewModel = MatchHistoryViewModel(getMatchesFlowUseCaseStub, removeMatchUseCaseSpy)

    @Test
    fun `should fetch matches from use case`() = runTest {
        matchHistoryViewModel.uiModel.test {
            val loading = awaitItem()
            loading.shouldBeInstanceOf<MatchHistoryUiModel.Loading>()

            val content = awaitItem()
            val value = content as MatchHistoryUiModel.Content
            value.matches shouldContainExactly expectedUiModel.matches
        }
    }

    @Test
    fun `flow update updates the ui state`() = runTest {
        matchHistoryViewModel.uiModel.test {
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
        matchHistoryViewModel.removeMatch(0L)

        removeMatchUseCaseSpy.removedMatchId shouldBe 0L
    }
}
