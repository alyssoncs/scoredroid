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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.editmatch.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.editmatch.ui.state.EditMatchUiState
import org.scoredroid.usecase.AddTeamRequest
import org.scoredroid.usecase.AddTeamUseCase
import org.scoredroid.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.CreateMatchUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.RemoveTeamUseCase
import org.scoredroid.usecase.RenameMatchUseCase
import org.scoredroid.usecase.RenameTeamUseCase
import org.scoredroid.usecase.SaveMatchUseCase

internal class EditMatchViewModel(
    private val createMatch: CreateMatchUseCase,
    private val getMatchFlow: GetMatchFlowUseCase,
    private val renameMatch: RenameMatchUseCase,
    private val renameTeam: RenameTeamUseCase,
    private val addTeam: AddTeamUseCase,
    private val removeTeam: RemoveTeamUseCase,
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

    private val shouldNavigateBack = MutableStateFlow(false)
    val uiState: StateFlow<EditMatchUiState> = flow {
        emitAll(combine(getMatchFlow(getMatchId()), shouldNavigateBack, ::toUiState))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = EditMatchUiState.Loading(shouldNavigateBack = false),
    )

    override fun onCleared() {
        viewModelScope.launch(NonCancellable) {
            getMatchIdSafe()?.let { clearTransientData(it) }
        }
    }

    private fun onMatchNameChange(newName: String) {
        viewModelScope.launch {
            renameMatch(getMatchId(), newName)
        }
    }

    private fun onTeamNameChange(teamAt: Int, newName: String) {
        viewModelScope.launch {
            renameTeam(getMatchId(), teamAt, newName)
        }
    }

    private fun onTeamRemoved(teamAt: Int) {
        viewModelScope.launch {
            removeTeam(getMatchId(), teamAt)
        }
    }

    private fun onAddTeam() {
        viewModelScope.launch {
            addTeam(getMatchId(), AddTeamRequest(""))
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            saveMatch(getMatchId())
            shouldNavigateBack.update { true }
        }
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

    private fun toUiState(
        matchResponse: MatchResponse?,
        shouldNavigateBack: Boolean,
    ): EditMatchUiState {
        return if (matchResponse == null) {
            EditMatchUiState.MatchNotFound(shouldNavigateBack = shouldNavigateBack)
        } else {
            EditMatchUiState.Content(
                matchName = matchResponse.name,
                teams = matchResponse.teams.mapIndexed { idx, teamResponse ->
                    EditMatchUiState.Content.Team(
                        name = teamResponse.name,
                        score = teamResponse.score,
                        onNameChange = { name ->
                            onTeamNameChange(idx, name)
                        },
                        onRemove = {
                            onTeamRemoved(idx)
                        },
                    )
                },
                shouldNavigateBack = shouldNavigateBack,
                onMatchNameChange = ::onMatchNameChange,
                onAddTeam = ::onAddTeam,
                onSave = ::onSave,
            )
        }
    }

    private suspend fun getMatchId(): Long {
        val matchId = getMatchIdSafe()
        check(matchId != null) { "Could not find a match id" }
        return matchId
    }

    private suspend fun getMatchIdSafe(): Long? {
        initJob.join()
        return savedStateHandle[MATCH_ID_NAV_ARG]
    }
}
