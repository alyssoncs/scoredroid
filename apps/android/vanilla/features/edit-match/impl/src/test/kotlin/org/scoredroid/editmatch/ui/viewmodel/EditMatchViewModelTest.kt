package org.scoredroid.editmatch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.editmatch.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.editmatch.ui.state.EditMatchUiState
import org.scoredroid.usecase.AddTeamRequest
import org.scoredroid.usecase.doubles.AddTeamSpy
import org.scoredroid.usecase.doubles.ClearTransientMatchDataSpy
import org.scoredroid.usecase.doubles.CreateMatchStub
import org.scoredroid.usecase.doubles.GetMatchFlowStub
import org.scoredroid.usecase.doubles.RenameMatchSpy
import org.scoredroid.usecase.doubles.RenameTeamSpy
import org.scoredroid.usecase.doubles.SaveMatchSpy
import org.scoredroid.viewmodel.CoroutineTestExtension
import org.scoredroid.viewmodel.callOnCleared
import kotlin.random.Random

@ExtendWith(CoroutineTestExtension::class)
class EditMatchViewModelTest {
    private val viewModel: EditMatchViewModel by lazy {
        EditMatchViewModel(
            createMatchStub,
            getMatchFlow,
            renameMatchSpy,
            renameTeamSpy,
            addTeamSpy,
            saveMatchSpy,
            clearTransientMatchDataSpy,
            savedStateHandle,
        )
    }

    private val createMatchStub = CreateMatchStub()
    private val getMatchFlow = GetMatchFlowStub()
    private val renameMatchSpy = RenameMatchSpy()
    private val renameTeamSpy = RenameTeamSpy()
    private val addTeamSpy = AddTeamSpy()
    private val saveMatchSpy = SaveMatchSpy()
    private val clearTransientMatchDataSpy = ClearTransientMatchDataSpy()
    private lateinit var savedStateHandle: SavedStateHandle

    @Nested
    inner class NewMatch {

        @BeforeEach
        fun setUp() {
            savedStateHandle = SavedStateHandle(emptyMap())
        }

        @Test
        fun `should display new match values`() = runTest {
            val matchResponse = MatchResponse(
                id = 5,
                name = "a brand new match",
                teams = listOf(TeamResponse(name = "a brand new team", score = 2)),
            )
            createMatchStub.response = matchResponse
            getMatchFlow.response = matchResponse

            viewModel.uiState.test {
                val loadingState = awaitItem()
                loadingState.shouldBeInstanceOf<EditMatchUiState.Loading>()
                val contentState = awaitItem() as EditMatchUiState.Content
                contentState.shouldBeInstanceOf<EditMatchUiState.Content>()
                contentState.matchName shouldBe "a brand new match"
                contentState.teams.first().name shouldBe "a brand new team"
                contentState.teams.first().score shouldBe 2
            }
        }

        @Test
        fun `should save the match id in the saved state handle`() = runTest {
            createMatchStub.response = MatchResponse(id = 5, name = "", teams = emptyList())
            savedStateHandle.contains(MATCH_ID_NAV_ARG).shouldBeFalse()

            viewModel.uiState.test {
                awaitItem()
                awaitItem()
            }

            savedStateHandle.get<Long>(MATCH_ID_NAV_ARG) shouldBe 5
        }
    }

    @Nested
    inner class ExistingMatch {

        @BeforeEach
        fun setUp() {
            savedStateHandle = SavedStateHandle(mapOf(MATCH_ID_NAV_ARG to 4L))
        }

        @Test
        fun `given non existing match, should display match not found`() = runTest {
            getMatchFlow.response = null

            viewModel.uiState.test {
                awaitItem().shouldBeInstanceOf<EditMatchUiState.Loading>()
                awaitItem().shouldBeInstanceOf<EditMatchUiState.MatchNotFound>()
            }
        }

        @Test
        fun `given existing match, should display match values`() = runTest {
            getMatchFlow.response = MatchResponse(
                id = 5,
                name = "good old match",
                teams = listOf(TeamResponse(name = "old team", score = 3)),
            )

            viewModel.uiState.test {
                val loadingState = awaitItem()
                loadingState.shouldBeInstanceOf<EditMatchUiState.Loading>()
                val contentState = awaitItem() as EditMatchUiState.Content
                contentState.shouldBeInstanceOf<EditMatchUiState.Content>()
                contentState.matchName shouldBe "good old match"
                contentState.teams.first().name shouldBe "old team"
                contentState.teams.first().score shouldBe 3
            }
        }
    }

    @Nested
    inner class OldOrNewMatch {

        private val matchId = 6L

        @BeforeEach
        fun setUp() {
            val matchResponse = createMatchStub.response.copy(id = matchId)
            createMatchStub.response = matchResponse
            getMatchFlow.response = matchResponse

            val shouldCreateNewMatch = Random.nextBoolean()
            val initialValue =
                if (shouldCreateNewMatch) emptyMap() else mapOf(MATCH_ID_NAV_ARG to matchId)
            savedStateHandle = SavedStateHandle(initialValue)
        }

        @Test
        fun `on match name change, call rename match`() = runTest {
            viewModel.onMatchNameChange("new name")

            delay(500)

            renameMatchSpy.matchWithId(matchId).wasRenamedTo("new name").shouldBeTrue()
        }

        @Test
        fun `on team name change, call rename team`() = runTest {
            viewModel.onTeamNameChange(0, "new name")

            delay(500)

            renameTeamSpy.team(matchId, 0).wasRenamedTo("new name").shouldBeTrue()
        }

        @Test
        fun `on add team, call add team`() = runTest {
            viewModel.onAddTeam()

            delay(500)

            addTeamSpy.matchWithId(matchId).hasNewTeam(AddTeamRequest("")).shouldBeTrue()
        }

        @Test
        fun `on save, call save match`() = runTest {
            viewModel.onSave()

            delay(500)

            saveMatchSpy.matchWithId(matchId).wasSaved().shouldBeTrue()
        }

        @Test
        fun `on save, navigate back`() = runTest {
            viewModel.onSave()

            viewModel.uiState.test {
                skipItems(1)
                awaitItem().shouldNavigateBack.shouldBeTrue()
            }
        }

        @Test
        fun `on cleared, call clear transient data`() = runTest {
            createViewModel()
            delay(500)

            viewModel.callOnCleared()

            clearTransientMatchDataSpy.matchWithId(matchId).wasCleared().shouldBeTrue()
        }

        private fun createViewModel() {
            viewModel.uiState
        }
    }
}
