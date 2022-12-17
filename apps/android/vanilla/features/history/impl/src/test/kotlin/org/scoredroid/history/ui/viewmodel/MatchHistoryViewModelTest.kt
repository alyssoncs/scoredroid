package org.scoredroid.history.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.viewmodel.CoroutineTestExtension

@ExtendWith(CoroutineTestExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MatchHistoryViewModelTest {

    private val matchResponse = listOf(
        MatchResponse(
            id = 5L,
            name = "match name",
            teams = listOf(TeamResponse(name = "team name", score = 0)),
        )
    )

    private val expectedUiModel = MatchHistoryUiModel.Content(
        matches = listOf(
            MatchHistoryUiModel.Content.Match(
                matchName = "match name",
                numberOfTeams = 1,
                id = 5L,
            )
        )
    )

    private val getMatchesUseCaseStub = GetMatchesUseCaseStub().also {
        it.theResponse = matchResponse
    }

    private val matchHistoryViewModel = MatchHistoryViewModel(getMatchesUseCaseStub)

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
    fun `on click, navigate to edit match`() = runTest {
        matchHistoryViewModel.navigateToEditScreen.test {
            assertThat(awaitItem()).isNull()

            matchHistoryViewModel.onClick(5L)

            assertThat(awaitItem()!!.matchId).isEqualTo(5L)
        }
    }

    @Test
    fun `on navigate to edit screen, set navigate to edit match as null`() = runTest {
        matchHistoryViewModel.navigateToEditScreen.test {
            assertThat(awaitItem()).isNull()
            matchHistoryViewModel.onClick(5L)
            assertThat(awaitItem()).isNotNull()

            matchHistoryViewModel.onNavigateToEditScreen()

            assertThat(awaitItem()).isNull()
        }
    }


    class GetMatchesUseCaseStub : GetMatchesUseCase {
        var theResponse: List<MatchResponse> = emptyList()

        override suspend fun invoke(): List<MatchResponse> {
            delay(1000)
            return theResponse
        }
    }
}