package org.scoredroid.editmatch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.scoredroid.data.response.MatchResponse
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

class EditMatchViewModel(
    private val createMatch: CreateMatchUseCase,
    private val getMatchFlow: GetMatchFlowUseCase,
    private val renameMatch: RenameMatchUseCase,
    private val renameTeam: RenameTeamUseCase,
    private val addTeam: AddTeamUseCase,
    private val saveMatch: SaveMatchUseCase,
    private val clearTransientData: ClearTransientMatchDataUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var initJob: Job
    init {
        initJob = viewModelScope.launch {
            ensureMatchExists()
        }
    }

    val uiState: StateFlow<EditMatchUiState> = flow {
        emitAll(
            getMatchFlow(getMatchId())
                .combine(_shouldNavigateBack) { matchState, shouldNavigateBack ->
                    combineState(matchState, shouldNavigateBack)
                }
                .map { combinedMatchState -> combinedMatchState.toUiState() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = EditMatchUiState.Loading(shouldNavigateBack = false),
    )

    private fun combineState(
        matchState: MatchResponse?,
        shouldNavigateBack: Boolean,
    ): CombinedMatchState {
        return CombinedMatchState(matchState, shouldNavigateBack)
    }

    private val _shouldNavigateBack = MutableStateFlow(false)

    override fun onCleared() {
        viewModelScope.launch(NonCancellable) {
            getMatchIdSafe()?.let { clearTransientData(it) }
        }
    }

    fun onMatchNameChange(newName: String) {
        viewModelScope.launch {
            renameMatch(getMatchId(), newName)
        }
    }

    fun onTeamNameChange(teamAt: Int, newName: String) {
        viewModelScope.launch {
            renameTeam(getMatchId(), teamAt, newName)
        }
    }

    fun onAddTeam() {
        viewModelScope.launch {
            addTeam(getMatchId(), AddTeamRequest(""))
        }
    }

    fun onSave() {
        viewModelScope.launch {
            saveMatch(getMatchId())
            _shouldNavigateBack.update { true }
        }
    }

    fun onNavigateBack() {
        _shouldNavigateBack.update { false }
    }

    private suspend fun ensureMatchExists() {
        val matchId = savedStateHandle.get<Long>(MATCH_ID_NAV_ARG)
        if (matchId == null) {
            savedStateHandle[MATCH_ID_NAV_ARG] = createEmptyMatch().id
        }
    }

    private suspend fun createEmptyMatch(): MatchResponse {
        return createMatch(CreateMatchRequestOptions())
    }

    private fun CombinedMatchState.toUiState(): EditMatchUiState {
        return if (this.matchState == null) {
            EditMatchUiState.MatchNotFound(shouldNavigateBack = this.shouldNavigateBack)
        } else {
            EditMatchUiState.Content(
                matchName = this.matchState.name,
                teams = this.matchState.teams.map { teamResponse ->
                    EditMatchUiState.Content.Team(
                        name = teamResponse.name,
                        score = teamResponse.score,
                    )
                },
                shouldNavigateBack = this.shouldNavigateBack,
            )
        }
    }

    private suspend fun getMatchId(): Long {
        return getMatchIdSafe()!!
    }

    private suspend fun getMatchIdSafe(): Long? {
        initJob.join()
        return savedStateHandle[MATCH_ID_NAV_ARG]
    }

    private data class CombinedMatchState(
        val matchState: MatchResponse?,
        val shouldNavigateBack: Boolean,
    )
}
