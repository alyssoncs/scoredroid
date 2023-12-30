package org.scoredroid.editmatch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.TurbineTestContext
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
import org.scoredroid.usecase.test.doubles.AddTeamSpy
import org.scoredroid.usecase.test.doubles.ClearTransientMatchDataSpy
import org.scoredroid.usecase.test.doubles.CreateMatchStub
import org.scoredroid.usecase.test.doubles.GetMatchFlowStub
import org.scoredroid.usecase.test.doubles.RenameMatchSpy
import org.scoredroid.usecase.test.doubles.RenameTeamSpy
import org.scoredroid.usecase.test.doubles.SaveMatchSpy
import org.scoredroid.viewmodel.CoroutineTestExtension
import org.scoredroid.viewmodel.callOnCleared

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
    inner class NewMatch : OldOrNewMatchTests() {

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
    inner class ExistingMatch : OldOrNewMatchTests() {

        @BeforeEach
        fun setUp() {
            savedStateHandle = SavedStateHandle(mapOf(MATCH_ID_NAV_ARG to matchId))
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

    abstract inner class OldOrNewMatchTests {

        protected val matchId = 4L

        @BeforeEach
        fun setUpCreateMatchAndGetMatchFlow() {
            val matchResponse = createMatchStub.response.copy(id = matchId)
            createMatchStub.response = matchResponse
            getMatchFlow.response = matchResponse
        }

        @Test
        fun `on match name change, call rename match`() = runTest {
            viewModel.uiState.test {
                skipLoading()
                awaitItem().asContent().onMatchNameChange("new name")
            }

            renameMatchSpy.matchWithId(matchId).wasRenamedTo("new name").shouldBeTrue()
        }

        @Test
        fun `on team name change, call rename team`() = runTest {
            viewModel.uiState.test {
                skipLoading()
                awaitItem().asContent().teams[0].onNameChange("new name")
            }

            renameTeamSpy.team(matchId, 0).wasRenamedTo("new name").shouldBeTrue()
        }

        @Test
        fun `on add team, call add team`() = runTest {
            viewModel.uiState.test {
                skipLoading()
                awaitItem().asContent().onAddTeam()
            }

            addTeamSpy.matchWithId(matchId).hasNewTeam(AddTeamRequest("")).shouldBeTrue()
        }

        @Test
        fun `on save, call save match`() = runTest {
            viewModel.uiState.test {
                skipLoading()
                awaitItem().asContent().onSave()
                cancelAndIgnoreRemainingEvents()
            }

            saveMatchSpy.matchWithId(matchId).wasSaved().shouldBeTrue()
        }

        @Test
        fun `on save, navigate back`() = runTest {
            viewModel.uiState.test {
                skipLoading()
                awaitItem().asContent().onSave()
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

        private suspend fun TurbineTestContext<EditMatchUiState>.skipLoading() {
            skipItems(1)
        }

        private fun EditMatchUiState.asContent() = this as EditMatchUiState.Content
    }
}
