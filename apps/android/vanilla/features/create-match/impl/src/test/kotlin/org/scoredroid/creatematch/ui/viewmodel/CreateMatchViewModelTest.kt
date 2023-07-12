package org.scoredroid.creatematch.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.match.domain.request.CreateMatchRequestOptions
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.viewmodel.CoroutineTestExtension

@ExtendWith(CoroutineTestExtension::class)
class CreateMatchViewModelTest {
    private val createMatch = CreateMatchUseCaseSpy()
    private val viewModel by lazy {
        CreateMatchViewModel(createMatch)
    }

    @Test
    fun `uiState should start empty`() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState.matchName).isEmpty()
            assertThat(uiState.teams).isEmpty()
            assertThat(uiState.loading).isFalse()
        }
    }

    @Test
    fun `on match name change, should update uiState`() = runTest {
        viewModel.onMatchNameChange("final match")

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState.matchName).isEqualTo("final match")
        }
    }

    @Test
    fun `on team name change, should update uiState`() = runTest {
        viewModel.onAddTeam()
        viewModel.onTeamNameChange(index = 0, newName = "best team")

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState.teams.first()).isEqualTo("best team")
        }
    }

    @Test
    fun `on create, should call use case`() = runTest {
        viewModel.onMatchNameChange("best match")
        viewModel.onAddTeam()
        viewModel.onAddTeam()
        viewModel.onTeamNameChange(index = 0, newName = "best team")
        viewModel.onTeamNameChange(index = 1, newName = "worst team")
        viewModel.onCreate()

        assertThat(createMatch.request)
            .isEqualTo(
                CreateMatchRequestOptions(
                    matchName = "best match",
                    teams = listOf(
                        CreateMatchRequestOptions.InitialTeamRequest(name = "best team"),
                        CreateMatchRequestOptions.InitialTeamRequest(name = "worst team"),
                    ),
                ),
            )
    }

    @Test
    fun `on create, should load while creating`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreate()

            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().loading).isFalse()
        }
    }

    class CreateMatchUseCaseSpy : CreateMatchUseCase {
        var request: CreateMatchRequestOptions? = null
            private set

        private val response: MatchResponse = MatchResponse(
            id = 0,
            name = "",
            teams = listOf(
                TeamResponse(name = "first team", score = 0),
                TeamResponse(name = "second team", score = 0),
            ),
        )

        override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
            request = createMatchOptions
            return response
        }
    }
}
