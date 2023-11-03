package org.scoredroid.play.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.match.domain.usecase.SaveMatchUseCase
import org.scoredroid.play.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.play.ui.state.PlayUiState
import org.scoredroid.score.domain.usecase.DecrementScoreUseCase
import org.scoredroid.score.domain.usecase.IncrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.viewmodel.CoroutineTestExtension
import org.scoredroid.viewmodel.callOnCleared
import java.lang.IllegalStateException

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

            assertThat(saveMatchSpy.matchId).isEqualTo(1)
        }
    }

    class GetMatchFlowStub : GetMatchFlowUseCase {
        var response: MatchResponse? = null

        override suspend fun invoke(matchId: Long): Flow<MatchResponse?> {
            return flowOf(response)
        }
    }

    class IncrementScoreSpy : IncrementScoreUseCase {
        var matchId: Long? = null
            private set
        var teamAt: Int? = null
            private set
        var increment: Int? = null
            private set
        private var response: MatchResponse? = null

        fun setResponse(response: MatchResponse) {
            this.response = response
        }

        override suspend fun invoke(
            matchId: Long,
            teamAt: Int,
            increment: Int,
        ): Result<MatchResponse> {
            this.matchId = matchId
            this.teamAt = teamAt
            this.increment = increment

            return if (response != null) {
                Result.success(response!!)
            } else {
                Result.failure(IllegalStateException("Spy not configured. Call setResponse"))
            }
        }
    }

    class DecrementScoreSpy : DecrementScoreUseCase {
        var matchId: Long? = null
            private set
        var teamAt: Int? = null
            private set
        var decrement: Int? = null
            private set
        private var response: MatchResponse? = null

        fun setResponse(response: MatchResponse) {
            this.response = response
        }

        override suspend fun invoke(
            matchId: Long,
            teamAt: Int,
            decrement: Int,
        ): Result<MatchResponse> {
            this.matchId = matchId
            this.teamAt = teamAt
            this.decrement = decrement

            return if (response != null) {
                Result.success(response!!)
            } else {
                Result.failure(IllegalStateException("Spy not configured. Call setResponse"))
            }
        }
    }

    class SaveMatchSpy : SaveMatchUseCase {
        var matchId: Long? = null
            private set

        override suspend fun invoke(matchId: Long): Result<Unit> {
            this.matchId = matchId
            return Result.success(Unit)
        }
    }
}
