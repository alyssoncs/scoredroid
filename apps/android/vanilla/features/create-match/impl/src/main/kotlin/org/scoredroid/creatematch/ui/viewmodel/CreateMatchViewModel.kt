package org.scoredroid.creatematch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.scoredroid.creatematch.ui.state.CreateMatchUiState
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.CreateMatchUseCase

class CreateMatchViewModel(
    private val createMatch: CreateMatchUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateMatchUiState(
            onAddTeam = ::onAddTeam,
            onMatchNameChange = ::onMatchNameChange,
            onTeamNameChange = ::onTeamNameChange,
            onCreate = ::onCreate,
        ),
    )

    val uiState = _uiState.asStateFlow()

    private fun onMatchNameChange(newName: String) {
        _uiState.update { currentState -> currentState.copy(matchName = newName) }
    }

    private fun onTeamNameChange(teamAt: Int, newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                teams = currentState.teams.mapIndexed { currentIndex, currentName ->
                    if (currentIndex == teamAt) newName else currentName
                },
            )
        }
    }

    private fun onAddTeam() {
        _uiState.update { currentState ->
            currentState.copy(
                teams = currentState.teams + "",
            )
        }
    }

    private fun onCreate() {
        _uiState.update { currentState -> currentState.copy(loading = true) }
        viewModelScope.launch {
            createMatch(uiState.value.toCreateMatchRequest())
        }
        _uiState.update { currentState -> currentState.copy(loading = false, created = true) }
    }

    private fun CreateMatchUiState.toCreateMatchRequest(): CreateMatchRequestOptions {
        return CreateMatchRequestOptions(
            matchName = matchName,
            teams = teams.map { teamName ->
                CreateMatchRequestOptions.InitialTeamRequest(name = teamName)
            },
        )
    }
}
