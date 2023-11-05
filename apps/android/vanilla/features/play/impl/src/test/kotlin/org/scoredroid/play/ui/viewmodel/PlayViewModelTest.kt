package org.scoredroid.play.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.play.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.play.ui.state.PlayUiState
import org.scoredroid.usecase.doubles.DecrementScoreSpy
import org.scoredroid.usecase.doubles.GetMatchFlowStub
import org.scoredroid.usecase.doubles.IncrementScoreSpy
import org.scoredroid.usecase.doubles.SaveMatchSpy
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
                assertThat(awaitItem()).isEqualTo(PlayUiState.Error)
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
                assertThat(awaitItem()).isEqualTo(PlayUiState.Loading)
                assertThat(awaitItem()).isEqualTo(PlayUiState.Error)
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
        }

        @Test
        fun `should show content`() = runTest {
            viewModel.uiState.test {
                assertThat(awaitItem()).isEqualTo(PlayUiState.Loading)
                val content = awaitItem() as PlayUiState.Content
                assertThat(content.matchName).isEqualTo(matchResponse.name)
                assertThat(content.teams).hasSize(matchResponse.teams.size)
                assertThat(content.teams.first().name).isEqualTo(matchResponse.teams.first().name)
                assertThat(content.teams.first().score).isEqualTo(matchResponse.teams.first().score)
            }
        }

        @Test
        fun `incrementScore should call IncrementScore`() = runTest {
            incrementScoreSpy.setResponse(matchResponse)

            viewModel.incrementScore(teamAt = 0)

            assertThat(incrementScoreSpy.matchId).isEqualTo(matchResponse.id)
            assertThat(incrementScoreSpy.teamAt).isEqualTo(0)
            assertThat(incrementScoreSpy.increment).isEqualTo(1)
        }

        @Test
        fun `decrementScore should call DecrementScore`() = runTest {
            decrementScoreSpy.setResponse(matchResponse)

            viewModel.decrementScore(teamAt = 0)

            assertThat(decrementScoreSpy.matchId).isEqualTo(matchResponse.id)
            assertThat(decrementScoreSpy.teamAt).isEqualTo(0)
            assertThat(decrementScoreSpy.decrement).isEqualTo(1)
        }

        @Test
        fun `onCleared should call SaveMatch`() = runTest {
            viewModel.callOnCleared()

            assertThat(saveMatchSpy.matchWithId(1).wasSaved()).isTrue()
        }
    }
}
