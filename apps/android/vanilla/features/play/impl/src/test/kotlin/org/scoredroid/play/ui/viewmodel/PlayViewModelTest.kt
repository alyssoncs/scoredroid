package org.scoredroid.play.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.play.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.play.ui.state.PlayUiState
import org.scoredroid.usecase.test.doubles.DecrementScoreSpy
import org.scoredroid.usecase.test.doubles.GetMatchFlowStub
import org.scoredroid.usecase.test.doubles.IncrementScoreSpy
import org.scoredroid.usecase.test.doubles.SaveMatchSpy
import org.scoredroid.viewmodel.CoroutineTestExtension
import org.scoredroid.viewmodel.callOnCleared

@ExtendWith(CoroutineTestExtension::class)
class PlayViewModelTest {

    private val getMatchFlowStub = GetMatchFlowStub()
    private val incrementScoreSpy = IncrementScoreSpy()
    private val decrementScoreSpy = DecrementScoreSpy()
    private val saveMatchSpy = SaveMatchSpy()
    private lateinit var savedStateHandle: SavedStateHandle

    private val viewModel by lazy {
        PlayViewModel(
            getMatchFlowStub,
            incrementScoreSpy,
            decrementScoreSpy,
            saveMatchSpy,
            savedStateHandle,
        )
    }

    @Nested
    inner class NoId {
        @BeforeEach
        fun setup() {
            savedStateHandle = SavedStateHandle()
        }

        @Test
        fun `should show error`() = runTest {
            viewModel.uiState.test {
                awaitItem() shouldBe PlayUiState.Error
            }
        }
    }

    @Nested
    inner class MatchNotFound {
        @BeforeEach
        fun setup() {
            savedStateHandle = SavedStateHandle(mapOf(MATCH_ID_NAV_ARG to -1L))
            getMatchFlowStub.response = null
        }

        @Test
        fun `should show error`() = runTest {
            viewModel.uiState.test {
                awaitItem() shouldBe PlayUiState.Loading
                awaitItem() shouldBe PlayUiState.Error
            }
        }
    }

    @Nested
    inner class ExistingMatch {
        private val matchResponse = MatchResponse(
            id = 1,
            name = "match name",
            teams = listOf(
                TeamResponse(
                    name = "team name",
                    score = 5,
                ),
            ),
        )

        @BeforeEach
        fun setup() {
            savedStateHandle = SavedStateHandle(mapOf(MATCH_ID_NAV_ARG to 1L))
            getMatchFlowStub.response = matchResponse
            incrementScoreSpy.setResponse(matchResponse)
            decrementScoreSpy.setResponse(matchResponse)
        }

        @Test
        fun `should show content`() = runTest {
            viewModel.uiState.test {
                awaitItem() shouldBe PlayUiState.Loading
                val content = awaitItem() as PlayUiState.Content
                content.matchName shouldBe matchResponse.name
                content.teams shouldHaveSize matchResponse.teams.size
                content.teams.first().name shouldBe matchResponse.teams.first().name
                content.teams.first().score shouldBe matchResponse.teams.first().score
            }
        }

        @Test
        fun `incrementScore should call IncrementScore`() = runTest {
            viewModel.uiState.test {
                skipItems(1)
                (awaitItem() as PlayUiState.Content).teams[0].onIncrement()
            }

            incrementScoreSpy.matchId shouldBe matchResponse.id
            incrementScoreSpy.teamAt shouldBe 0
            incrementScoreSpy.increment shouldBe 1
        }

        @Test
        fun `decrementScore should call DecrementScore`() = runTest {
            viewModel.uiState.test {
                skipItems(1)
                (awaitItem() as PlayUiState.Content).teams[0].onDecrement()
            }

            decrementScoreSpy.matchId shouldBe matchResponse.id
            decrementScoreSpy.teamAt shouldBe 0
            decrementScoreSpy.decrement shouldBe 1
        }

        @Test
        fun `onCleared should call SaveMatch`() = runTest {
            viewModel.callOnCleared()

            saveMatchSpy.matchWithId(1).wasSaved().shouldBeTrue()
        }
    }
}
