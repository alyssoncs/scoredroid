package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.match.domain.usecase.GetMatchesUseCase

class MatchHistoryViewModel(
    private val getMatches: GetMatchesUseCase,
) : ViewModel() {

    val uiModel: StateFlow<MatchHistoryUiModel> = flow {
        emit(MatchHistoryUiModel.Content(getMatches().map(::toUiModel)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MatchHistoryUiModel.Loading,
    )

    data class Navigation(val matchId: Long)
    private val _navigateToEditScreen = MutableStateFlow<Navigation?>(null)
    val navigateToEditScreen = _navigateToEditScreen.asStateFlow()

    fun onClick(matchId: Long) {
        _navigateToEditScreen.update { Navigation(matchId) }
    }

    fun onNavigateToEditScreen() {
        _navigateToEditScreen.update { null }
    }

    private fun toUiModel(it: MatchResponse) = MatchHistoryUiModel.Content.Match(
        matchName = it.name,
        numberOfTeams = it.teams.count(),
        id = it.id,
    )
}