package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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

    private fun toUiModel(it: MatchResponse) = MatchHistoryUiModel.Content.Match(
        matchName = it.name,
        numberOfTeams = it.teams.count(),
        id = it.id,
    )
}
