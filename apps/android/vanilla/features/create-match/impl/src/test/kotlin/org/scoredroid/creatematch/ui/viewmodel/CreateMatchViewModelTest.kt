package org.scoredroid.creatematch.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.doubles.CreateMatchSpy
import org.scoredroid.viewmodel.CoroutineTestExtension

@ExtendWith(CoroutineTestExtension::class)
class CreateMatchViewModelTest {
    private val createMatch = CreateMatchSpy()
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
            assertThat(uiState.created).isFalse()
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

    @Test
    fun `on create, should update uiState to created`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreate()

            assertThat(awaitItem().created).isFalse()
            assertThat(awaitItem().created).isTrue()
        }
    }
}
