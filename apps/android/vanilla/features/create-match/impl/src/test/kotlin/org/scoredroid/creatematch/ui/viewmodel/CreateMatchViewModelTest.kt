package org.scoredroid.creatematch.ui.viewmodel

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.test.doubles.CreateMatchSpy
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

            uiState.matchName.shouldBeEmpty()
            uiState.teams.shouldBeEmpty()
            uiState.loading.shouldBeFalse()
            uiState.created.shouldBeFalse()
        }
    }

    @Test
    fun `on match name change, should update uiState`() = runTest {
        viewModel.uiState.test {
            awaitItem().onMatchNameChange("final match")

            awaitItem().matchName shouldBe "final match"
        }
    }

    @Test
    fun `on team name change, should update uiState`() = runTest {
        viewModel.uiState.test {
            awaitItem().onAddTeam()
            awaitItem().teams[0].onNameChange("best team")

            val uiState = awaitItem()

            uiState.teams.first().name shouldBe "best team"
        }
    }

    @Test
    fun `on remove team, should update uiState`() = runTest {
        viewModel.uiState.test {
            awaitItem().onAddTeam()
            awaitItem().teams[0].onRemove()

            val uiState = awaitItem()

            uiState.teams.shouldBeEmpty()
        }
    }

    @Test
    fun `remove team does not affect rename`() = runTest {
        viewModel.uiState.test {
            awaitItem().onAddTeam()
            awaitItem().onAddTeam()
            awaitItem().onAddTeam()
            awaitItem().teams[1].onRemove()
            awaitItem().teams[1].onNameChange("new name")

            val uiState = awaitItem()

            uiState.teams[1].name shouldBe "new name"
        }
    }

    @Test
    fun `on create, should call use case`() = runTest {
        viewModel.uiState.test {
            awaitItem().onMatchNameChange("best match")
            awaitItem().onAddTeam()
            awaitItem().onAddTeam()
            awaitItem().teams[0].onNameChange("best team")
            awaitItem().teams[1].onNameChange("worst team")
            awaitItem().onCreate()
            cancelAndIgnoreRemainingEvents()
        }

        createMatch.request shouldBe CreateMatchRequestOptions(
            matchName = "best match",
            teams = listOf(
                CreateMatchRequestOptions.InitialTeamRequest(name = "best team"),
                CreateMatchRequestOptions.InitialTeamRequest(name = "worst team"),
            ),
        )
    }

    @Test
    fun `on create, should load while creating`() = runTest {
        viewModel.uiState.test {
            awaitItem().onCreate()

            awaitItem().loading.shouldBeTrue()
            awaitItem().loading.shouldBeFalse()
        }
    }

    @Test
    fun `on create, should update uiState to created`() = runTest {
        viewModel.uiState.test {
            awaitItem().onCreate()

            awaitItem().created.shouldBeFalse()
            awaitItem().created.shouldBeTrue()
        }
    }
}
