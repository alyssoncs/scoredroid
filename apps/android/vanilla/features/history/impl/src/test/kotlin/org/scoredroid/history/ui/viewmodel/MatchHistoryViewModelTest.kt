package org.scoredroid.history.ui.viewmodel

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.match.domain.usecase.GetMatchesUseCase

@ExtendWith(CoroutineTestExtension::class)
class MatchHistoryViewModelTest {

    private val matchResponse = listOf(
        MatchResponse(
            id = 5L,
            name = "match name",
            teams = listOf(TeamResponse(name = "team name", score = 0)),
        )
    )

    private val expectedUiModel = MatchHistoryViewModel.UiModel(matchName = "match name", numberOfTeams = 1)

    private val getMatchesUseCaseStub = GetMatchesUseCaseStub().also {
        it.theResponse = matchResponse
    }

    private val matchHistoryViewModel = MatchHistoryViewModel(getMatchesUseCaseStub)

    @Test
    fun `should fetch matches from use case`() {
        assertThat(matchHistoryViewModel.matches.value).containsExactly(expectedUiModel)
    }

    class GetMatchesUseCaseStub : GetMatchesUseCase {
        var theResponse: List<MatchResponse> = emptyList()

        override suspend fun invoke(): List<MatchResponse> {
            return theResponse
        }
    }
}