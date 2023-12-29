package org.scoredroid.play.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.play.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.play.ui.state.PlayUiState
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.IncrementScoreUseCase
import org.scoredroid.usecase.SaveMatchUseCase

class PlayViewModel(
    private val getMatchFlow: GetMatchFlowUseCase,
    private val incrementScore: IncrementScoreUseCase,
    private val decrementScore: DecrementScoreUseCase,
    private val saveMatch: SaveMatchUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayUiState>(PlayUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val matchId = getMatchId()
        if (matchId == null) {
            _uiState.value = PlayUiState.Error
        } else {
            viewModelScope.launch {
                getMatchFlow(matchId).collect {
                    _uiState.value = it.toUiState()
                }
            }
        }
    }

    private fun incrementScore(teamAt: Int) {
        viewModelScope.launch {
            withMatchId { matchId ->
                incrementScore(matchId = matchId, teamAt = teamAt, increment = 1)
            }
        }
    }

    private fun decrementScore(teamAt: Int) {
        viewModelScope.launch {
            withMatchId { matchId ->
                decrementScore(matchId = matchId, teamAt = teamAt, decrement = 1)
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch(NonCancellable) {
            withMatchId { matchId ->
                saveMatch(matchId)
            }
        }
    }

    private inline fun PlayViewModel.withMatchId(action: (Long) -> Unit) {
        getMatchId()?.let(action)
    }

    private fun getMatchId(): Long? = savedStateHandle.get<Long>(MATCH_ID_NAV_ARG)

    private fun MatchResponse?.toUiState(): PlayUiState {
        return if (this == null) {
            PlayUiState.Error
        } else {
            PlayUiState.Content(
                matchName = name,
                teams = teams.mapIndexed { idx, team ->
                    PlayUiState.Content.Team(
                        name = team.name,
                        score = team.score,
                        onIncrement = { incrementScore(idx) },
                        onDecrement = { decrementScore(idx) },
                    )
                },
            )
        }
    }
}
