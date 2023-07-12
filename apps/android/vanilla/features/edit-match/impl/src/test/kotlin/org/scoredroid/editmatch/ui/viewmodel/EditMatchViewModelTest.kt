package org.scoredroid.editmatch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.editmatch.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.editmatch.ui.state.EditMatchUiState
import org.scoredroid.match.domain.request.CreateMatchRequestOptions
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.match.domain.usecase.GetMatchFlowUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase
import org.scoredroid.teams.domain.request.AddTeamRequest
import org.scoredroid.teams.domain.usecase.AddTeamUseCase
import org.scoredroid.teams.domain.usecase.RenameTeamUseCase
import org.scoredroid.viewmodel.CoroutineTestExtension
import org.scoredroid.viewmodel.callOnCleared
import kotlin.random.Random

@ExtendWith(CoroutineTestExtension::class)
class EditMatchViewModelTest {
    private val viewModel: EditMatchViewModel by lazy {
        EditMatchViewModel(
            createMatch,
            getMatchFlow,
            renameMatchSpy,
            renameTeamSpy,
            addTeamSpy,
            saveMatchSpy,
            clearTransientDataSpy,
            savedStateHandle,
        )
    }

    private val createMatch = CreateMatchUseCaseStub()
    private val getMatchFlow = GetMatchFlowUseCaseStub()
    private val renameMatchSpy = RenameMatchSpy()
    private val renameTeamSpy = RenameTeamSpy()
    private val addTeamSpy = AddTeamSpy()
    private val saveMatchSpy = SaveMatchSpy()
    private val clearTransientDataSpy = ClearTransientDataSpy()
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
            createMatch.response = matchResponse
            getMatchFlow.response = matchResponse

            viewModel.uiState.test {
                val loadingState = awaitItem()
                assertThat(loadingState).isInstanceOf(EditMatchUiState.Loading::class.java)
                val contentState = awaitItem() as EditMatchUiState.Content
                assertThat(contentState).isInstanceOf(EditMatchUiState.Content::class.java)
                assertThat(contentState.matchName).isEqualTo("a brand new match")
                assertThat(contentState.teams.first().name).isEqualTo("a brand new team")
                assertThat(contentState.teams.first().score).isEqualTo(2)
            }
        }

        @Test
        fun `should save the match id in the saved state handle`() = runTest {
            createMatch.response = MatchResponse(id = 5, name = "", teams = emptyList())
            assertThat(savedStateHandle.contains(MATCH_ID_NAV_ARG)).isFalse()

            viewModel.uiState.test {
                awaitItem()
                awaitItem()
            }

            assertThat(savedStateHandle.get<Long>(MATCH_ID_NAV_ARG)).isEqualTo(5)
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
                assertThat(awaitItem()).isInstanceOf(EditMatchUiState.Loading::class.java)
                assertThat(awaitItem()).isInstanceOf(EditMatchUiState.MatchNotFound::class.java)
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
                assertThat(loadingState).isInstanceOf(EditMatchUiState.Loading::class.java)
                val contentState = awaitItem() as EditMatchUiState.Content
                assertThat(contentState).isInstanceOf(EditMatchUiState.Content::class.java)
                assertThat(contentState.matchName).isEqualTo("good old match")
                assertThat(contentState.teams.first().name).isEqualTo("old team")
                assertThat(contentState.teams.first().score).isEqualTo(3)
            }
        }
    }

    @Nested
    inner class OldOrNewMatch {

        private val matchId = 6L

        @BeforeEach
        fun setUp() {
            val matchResponse = createMatch.response.copy(id = matchId)
            createMatch.response = matchResponse
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

            assertThat(renameMatchSpy.matchWithId(matchId).wasRenamedTo("new name")).isTrue()
        }

        @Test
        fun `on team name change, call rename team`() = runTest {
            viewModel.onTeamNameChange(0, "new name")

            delay(500)

            assertThat(renameTeamSpy.team(matchId, 0).wasRenamedTo("new name")).isTrue()
        }

        @Test
        fun `on add team, call add team`() = runTest {
            viewModel.onAddTeam()

            delay(500)

            assertThat(addTeamSpy.matchWithId(matchId).hasNewTeam(AddTeamRequest(""))).isTrue()
        }

        @Test
        fun `on save, call save match`() = runTest {
            viewModel.onSave()

            delay(500)

            assertThat(saveMatchSpy.matchWithId(matchId).wasSaved()).isTrue()
        }

        @Test
        fun `on save, navigate back`() = runTest {
            viewModel.onSave()

            viewModel.uiState.test {
                skipItems(1)
                assertThat(awaitItem().shouldNavigateBack).isTrue()
            }
        }

        @Test
        fun `on cleared, call clear transient data`() = runTest {
            createViewModel()
            delay(500)

            viewModel.callOnCleared()

            assertThat(clearTransientDataSpy.matchWithId(matchId).wasCleared()).isTrue()
        }

        @Test
        fun `on navigate back, navigate back should be false`() = runTest {
            viewModel.uiState.test {
                skipItems(1)
                viewModel.onSave()
                assertThat(awaitItem().shouldNavigateBack).isTrue()

                viewModel.onNavigateBack()

                assertThat(awaitItem().shouldNavigateBack).isFalse()
            }
        }

        private fun createViewModel() {
            viewModel.uiState
        }
    }

    class CreateMatchUseCaseStub : CreateMatchUseCase {
        var response: MatchResponse = MatchResponse(
            id = 0,
            name = "",
            teams = listOf(
                TeamResponse(name = "first team", score = 0),
                TeamResponse(name = "second team", score = 0),
            ),
        )

        override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
            delay(500L)
            return response
        }
    }

    class GetMatchFlowUseCaseStub : GetMatchFlowUseCase {
        var response: MatchResponse? = null

        override suspend fun invoke(matchId: Long): Flow<MatchResponse?> {
            delay(500L)
            return flowOf(response)
        }
    }

    class RenameMatchSpy : RenameMatchUseCase {

        private val invocations = mutableMapOf<Long, String>()

        override suspend fun invoke(matchId: Long, name: String): Result<MatchResponse> {
            invocations[matchId] = name
            return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
        }

        fun matchWithId(matchId: Long) = object : Assertions {
            override fun wasRenamedTo(name: String): Boolean {
                return invocations[matchId] == name
            }
        }

        interface Assertions {
            fun wasRenamedTo(name: String): Boolean
        }
    }

    class RenameTeamSpy : RenameTeamUseCase {

        private val invocations = mutableMapOf<Pair<Long, Int>, String>()

        override suspend fun invoke(
            matchId: Long,
            teamAt: Int,
            newName: String,
        ): Result<MatchResponse> {
            invocations[matchId to teamAt] = newName
            return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
        }

        fun team(matchId: Long, teamAt: Int) = object : Assertions {
            override fun wasRenamedTo(name: String): Boolean {
                return invocations[matchId to teamAt] == name
            }
        }

        interface Assertions {
            fun wasRenamedTo(name: String): Boolean
        }
    }

    class AddTeamSpy : AddTeamUseCase {

        private val invocations = mutableMapOf<Long, AddTeamRequest>()

        override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
            invocations[matchId] = team
            return Result.success(MatchResponse(id = 0, name = "", teams = emptyList()))
        }

        fun matchWithId(matchId: Long) = object : Assertions {
            override fun hasNewTeam(team: AddTeamRequest): Boolean {
                return invocations[matchId] == team
            }
        }

        interface Assertions {
            fun hasNewTeam(team: AddTeamRequest): Boolean
        }
    }

    class SaveMatchSpy : SaveMatchUseCase {

        private val invocations = hashSetOf<Long>()

        override suspend fun invoke(matchId: Long): Result<Unit> {
            invocations.add(matchId)
            return Result.success(Unit)
        }

        fun matchWithId(matchId: Long) = object : Assertions {
            override fun wasSaved(): Boolean {
                return invocations.contains(matchId)
            }
        }

        interface Assertions {
            fun wasSaved(): Boolean
        }
    }

    class ClearTransientDataSpy : ClearTransientMatchDataUseCase {

        private val invocations = hashSetOf<Long>()

        override suspend fun invoke(matchId: Long): Result<Unit> {
            invocations.add(matchId)
            return Result.success(Unit)
        }

        fun matchWithId(matchId: Long) = object : Assertions {
            override fun wasCleared(): Boolean {
                return invocations.contains(matchId)
            }
        }

        interface Assertions {
            fun wasCleared(): Boolean
        }
    }
}
