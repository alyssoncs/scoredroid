package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.RemoveMatchUseCase

internal class MatchHistoryViewModel(
    private val getMatchesFlow: GetMatchesFlowUseCase,
    private val removeMatch: RemoveMatchUseCase,
) : ViewModel() {

    val uiState: StateFlow<MatchHistoryUiModel> = flow {
        getMatchesFlow().collect {
            emit(MatchHistoryUiModel.Content(it.map(::toUiModel)))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MatchHistoryUiModel.Loading,
    )

    private fun removeMatch(matchId: Long) {
        viewModelScope.launch {
            removeMatch.invoke(matchId)
        }
    }

    private fun toUiModel(it: MatchResponse) = MatchHistoryUiModel.Content.Match(
        matchName = it.name,
        numberOfTeams = it.teams.count(),
        id = it.id,
        onRemove = { removeMatch(it.id) },
    )
}
